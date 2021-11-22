package ikexing.atutils.core.ritual.entity;

import cn.hutool.core.lang.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.network.NetworkManager;
import ikexing.atutils.core.ritual.RitualMagneticAttraction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntityRitualMagneticAttraction extends EntityRitualBase {

    private boolean finish = false;
    private final int interval = 100 + rand.nextInt(101);
    private final Set<BlockPos> searchedPos = Sets.newHashSet();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    private final List<Pair<String, String>> oresTransform = Lists.newArrayList(
            Pair.of("oreIron", "nuggetIron"),
            Pair.of("oreNickel", "nuggetNickel"),
            Pair.of("oreCrudeSteel", "nuggetCrudeSteel"),
            Pair.of("blockRustyIron", "ingotIron")
    );

    private final RitualMagneticAttraction ritual;

    public EntityRitualMagneticAttraction(World worldIn) {
        super(worldIn);
        getDataManager().register(lifetime, ATUtils.ritualMa.getDuration() + 20);
        ritual = (RitualMagneticAttraction) ATUtils.ritualMa;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) return;

        if (ticksExisted % interval == 0 && !finish) {
            doExec();
        }
    }

    private void doExec() {
        BlockPos posA = new BlockPos(posX + (ritual.radius_x), posY + (ritual.radius_y), posZ + (ritual.radius_z));
        BlockPos posB = new BlockPos(posX - (ritual.radius_x), posY - (ritual.radius_y), posZ - (ritual.radius_z));

        Iterable<BlockPos> allInBox = BlockPos.getAllInBox(posA, posB);
        for (BlockPos pos : allInBox) {

            if (searchedPos.contains(pos)) continue;

            searchedPos.add(pos);

            IBlockState state = world.getBlockState(pos);
            IItemStack crtStack = CraftTweakerMC.getIItemStack(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
            List<IOreDictEntry> ores = Objects.nonNull(crtStack) ? crtStack.getOres() : Collections.emptyList();
            for (IOreDictEntry ore : ores) {
                for (Pair<String, String> transform : oresTransform) {
                    if (ore.getName().equals(transform.getKey())) {
                        threadPool.submit(doLast(transform.getKey(), transform.getValue(), pos));
                        return;
                    }
                }
            }
        }

        finish = true;
    }

    private Thread doLast(String input, String output, BlockPos pos) {
        return SidedThreadGroups.SERVER.newThread(() -> {
            if (isDead) return;
            NetworkManager.MagneticAttraction.sendClientCustomPacket(pos, output, world.provider.getDimension());
            try {
                if (isDead) return;
                Thread.sleep((10 + rand.nextInt(10)) * 1000);
                spawnItem(input, output, pos);
            } catch (InterruptedException ignored) {}
        });
    }

    private void spawnItem(String input, String output, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getMaterial() == Material.AIR) return;

        ItemStack stack = state.getBlock().getItem(world, pos, state);
        boolean ifExist = OreDictionary.getOres(input).stream().anyMatch(stack::isItemEqual);
        if (ifExist) {
            ItemStack res = OreDictionary.getOres(output).get(0).copy();
            res.setCount(3 + rand.nextInt(5));
            world.setBlockToAir(pos);
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.spawnEntity(new EntityItem(world, posX, posY + 1, posZ, res));
        }
    }

    @Override
    public void setDead() {
        this.isDead = true;
        threadPool.shutdownNow();
    }
}
