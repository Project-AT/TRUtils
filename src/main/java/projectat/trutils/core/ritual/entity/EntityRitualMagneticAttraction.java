package projectat.trutils.core.ritual.entity;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import projectat.trutils.Main;
import projectat.trutils.client.render.BlockOutlineRender;
import projectat.trutils.core.ritual.RitualMagneticAttraction;
import projectat.trutils.core.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.client.fx.FXWisp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vazkii.botania.common.block.tile.mana.TilePool.PARTICLE_COLOR;

public class EntityRitualMagneticAttraction extends EntityRitualBase {

    public static final DataParameter<Optional<BlockPos>> transformingPos = EntityDataManager.createKey(EntityRitualMagneticAttraction.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public static final DataParameter<Boolean> renderParticles = EntityDataManager.createKey(EntityRitualMagneticAttraction.class, DataSerializers.BOOLEAN);
    private static final List<Pair<String, String>> oresTransform = Lists.newArrayList(
            Pair.of("oreIron", "nuggetIron"),
            Pair.of("oreNickel", "nuggetNickel"),
            Pair.of("oreCrudeSteel", "nuggetCrudeSteel"),
            Pair.of("blockRustyIron", "ingotIron")
    );
    //仪式最多同时转换的矿石个数
    private static final int searchInterval = 20;
    private final RitualMagneticAttraction ritual;
    private int nextSearch = 0;
    private IBlockState transformingOre = null;
    private int remainingTicks = 0;
    private Map<IBlockState, ItemStack> recipe = null;

    public EntityRitualMagneticAttraction(World worldIn) {
        super(worldIn);
        getDataManager().register(lifetime, Main.ritualMa.getDuration() + 20);
        getDataManager().register(transformingPos, Optional.absent());
        getDataManager().register(renderParticles, false);
        ritual = (RitualMagneticAttraction) Main.ritualMa;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (isDead) {
            return;
        }
        if (world.isRemote) {
            playEffects();
            return;
        }

        getDataManager().set(renderParticles, remainingTicks > 30);
        if (!getDataManager().get(transformingPos).isPresent()) {
            if (nextSearch <= 0) {
                searchPossibleOre();
                nextSearch = searchInterval;
            } else {
                nextSearch--;
            }
        }

        processTransform();
    }

    private void playEffects() {
        Optional<BlockPos> blockPos = getDataManager().get(transformingPos);
        if (blockPos.isPresent()) {
            BlockPos pos = blockPos.get();
            //粒子效果

            if (getDataManager().get(renderParticles)) {
                double fxX = pos.getX() + 0.3 + Math.random() * 0.5;
                double fxY = pos.getY() + 0.6 + Math.random() * 0.25;
                double fxZ = pos.getZ() + Math.random();

                float fxR = PARTICLE_COLOR.getRed() / 255F;
                float fxG = PARTICLE_COLOR.getGreen() / 255F;
                float fxB = PARTICLE_COLOR.getBlue() / 255F;

                float fxSize = (float) (Math.random() / 3F);
                float fxMotionY = (float) (Math.random() / 25F);

                FXWisp wisp = new FXWisp(world, fxX, fxY, fxZ, fxSize, fxR, fxG, fxB, true, false, 2F);
                wisp.setSpeed(0, fxMotionY, 0);
                ObfuscationReflectionHelper.setPrivateValue(Particle.class, wisp, false, "field_190017_n");
                Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
            }


            BlockOutlineRender.INSTANCE.getPositionProviders().putIfAbsent(this, pos);
        } else {
            BlockOutlineRender.INSTANCE.getPositionProviders().remove(this);
        }
    }

    @Override
    public void setDead() {
        if (world.isRemote) {
            BlockOutlineRender.INSTANCE.getPositionProviders().remove(this);
        }
        super.setDead();
    }

    private void doTransform(BlockPos pos, IBlockState state) {
        world.setBlockToAir(pos);
        ItemStack result = recipe.get(state).copy();
        result.setCount(3 + rand.nextInt(4));
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.spawnEntity(new EntityItem(world, posX, posY + 1, posZ, result));
    }

    private void processTransform() {

        BlockPos pos = getDataManager().get(transformingPos).orNull();
        if (pos == null) {
            return;
        }

        IBlockState state = world.getBlockState(pos);
        if (state != transformingOre) {
            getDataManager().set(transformingPos, Optional.absent());
            transformingOre = null;
            nextSearch = 0;
            return;
        }

        if (--remainingTicks < 0) {
            getDataManager().set(transformingPos, Optional.absent());
            transformingOre = null;
            nextSearch = 0;
            doTransform(pos, state);
        }
    }

    private void searchPossibleOre() {
        initRecipe();


        BlockPos posA = new BlockPos(posX + (ritual.radius_x), posY + (ritual.radius_y), posZ + (ritual.radius_z));
        BlockPos posB = new BlockPos(posX - (ritual.radius_x), posY - (ritual.radius_y), posZ - (ritual.radius_z));

        Iterable<BlockPos> allInBox = BlockPos.getAllInBox(posA, posB);

        for (BlockPos pos : allInBox) {
            IBlockState blockState = world.getBlockState(pos);
            if (blockState.getBlockHardness(world, pos) < 0) {
                continue;
            }
            if (recipe.containsKey(blockState)) {
                getDataManager().set(transformingPos, Optional.of(pos));
                transformingOre = blockState;
                remainingTicks = randomInterval();
                return;
            }
        }

    }

    private int randomInterval() {

        return 100 + rand.nextInt(101);
    }

    private void initRecipe() {
        if (recipe == null) {
            recipe = new HashMap<>();

            for (Pair<String, String> entry : oresTransform) {
                NonNullList<ItemStack> items = OreDictionary.getOres(entry.getValue());
                if (items.isEmpty()) {
                    Main.logger.error("Could not find {} for ore transform", entry.getValue());
                    continue;
                }

                for (IBlockState state : Utils.getBlockStatesByOreDict(entry.getKey())) {
                    recipe.put(state, items.get(0));
                }
            }

        }
    }

}
