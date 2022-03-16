package ikexing.atutils.core.ritual.entity;

import com.google.common.collect.Lists;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import ikexing.atutils.ATUtils;
import ikexing.atutils.client.render.BlockOutlineRender;
import ikexing.atutils.core.ritual.RitualMagneticAttraction;
import ikexing.atutils.core.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.common.Botania;

import java.io.IOException;
import java.util.*;

import static ikexing.atutils.core.utils.CustomDataSerializers.SERIALIZER_BLOCK_POS_SET;
import static vazkii.botania.common.block.tile.mana.TilePool.PARTICLE_COLOR;

public class EntityRitualMagneticAttraction extends EntityRitualBase {

    private static final List<Pair<String, String>> oresTransform = Lists.newArrayList(
        Pair.of("oreIron", "nuggetIron"),
        Pair.of("oreNickel", "nuggetNickel"),
        Pair.of("oreCrudeSteel", "nuggetCrudeSteel"),
        Pair.of("blockRustyIron", "ingotIron")
    );

    //仪式最多同时转换的矿石个数
    private static final int maxParallel = 5;
    private static final int searchInterval = 20;


    private final RitualMagneticAttraction ritual;

    public EntityRitualMagneticAttraction(World worldIn) {
        super(worldIn);
        getDataManager().register(lifetime, ATUtils.ritualMa.getDuration() + 20);
        getDataManager().register(processingPlaces, processingMap.keySet());
        ritual = (RitualMagneticAttraction) ATUtils.ritualMa;
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

        if (ticksExisted % searchInterval == 0) {
            searchPossibleOres();
        }

        processTransform();
    }

    private void playEffects() {
        Set<BlockPos> blockPos = getDataManager().get(processingPlaces);
        for (BlockPos pos : blockPos) {
            Botania.proxy.wispFX(pos.getX() + 0.3 + Math.random() * 0.5, pos.getY() + 0.6 + Math.random() * 0.25, pos.getZ() + Math.random(), PARTICLE_COLOR.getRed() / 255F, PARTICLE_COLOR.getGreen() / 255F, PARTICLE_COLOR.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F, 2F);
        }

        if (!blockPos.isEmpty()) {
            BlockOutlineRender.INSTANCE.getPositionProviders().putIfAbsent(this, () -> getDataManager().get(processingPlaces));
        } else {
            BlockOutlineRender.INSTANCE.getPositionProviders().remove(this);
        }
    }

    @Override
    public void setDead() {
        if(world.isRemote) {
            BlockOutlineRender.INSTANCE.getPositionProviders().remove(this);
        }
        super.setDead();
    }

    private static DataParameter<Set<BlockPos>> processingPlaces = EntityDataManager.createKey(EntityRitualMagneticAttraction.class, SERIALIZER_BLOCK_POS_SET);

    private Map<BlockPos, Pair<IBlockState, MutableInt>> processingMap = new HashMap<>();

    private void doTransform(BlockPos pos, IBlockState state) {
        world.setBlockToAir(pos);
        ItemStack result = recipe.get(state).copy();
        result.setCount(3 + rand.nextInt(4));
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.spawnEntity(new EntityItem(world, posX, posY + 1, posZ, result));
    }

    private void processTransform() {
        Iterator<Map.Entry<BlockPos, Pair<IBlockState, MutableInt>>> iterator = processingMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Pair<IBlockState, MutableInt>> next = iterator.next();
            BlockPos pos = next.getKey();
            IBlockState transforming = next.getValue().getLeft();
            IBlockState state = world.getBlockState(pos);
            if (transforming != state) {
                iterator.remove();
                getDataManager().setDirty(processingPlaces);
                continue;
            }
            MutableInt ticksLeft = next.getValue().getRight();
            if (ticksLeft.decrementAndGet() < 0) {
                iterator.remove();
                getDataManager().setDirty(processingPlaces);
                doTransform(pos, state);
            }

        }
    }


    private void searchPossibleOres() {
        initRecipe();

        int processing = processingMap.size();
        if (processing > maxParallel) {
            return;
        }

        BlockPos posA = new BlockPos(posX + (ritual.radius_x), posY + (ritual.radius_y), posZ + (ritual.radius_z));
        BlockPos posB = new BlockPos(posX - (ritual.radius_x), posY - (ritual.radius_y), posZ - (ritual.radius_z));

        Iterable<BlockPos> allInBox = BlockPos.getAllInBox(posA, posB);

        for (BlockPos pos : allInBox) {
            if (processingMap.containsKey(pos)) {
                continue;
            }
            IBlockState blockState = world.getBlockState(pos);
            if (recipe.containsKey(blockState)) {
                processingMap.put(pos, Pair.of(blockState, new MutableInt(randomInterval())));
                getDataManager().setDirty(processingPlaces);
                processing++;
                if (processing > maxParallel) {
                    break;
                }
            }
        }

    }

    private int randomInterval() {

        return 100 + rand.nextInt(101);
    }

    private Map<IBlockState, ItemStack> recipe = null;

    private void initRecipe() {
        if (recipe == null) {
            recipe = new HashMap<>();

            for (Pair<String, String> entry : oresTransform) {
                NonNullList<ItemStack> items = OreDictionary.getOres(entry.getValue());
                if (items.isEmpty()) {
                    ATUtils.logger.error("Could not find {} for ore transform", entry.getValue());
                    continue;
                }

                for (IBlockState state : Utils.getBlockStatesByOreDict(entry.getKey())) {
                    recipe.put(state, items.get(0));
                }
            }

        }
    }




}
