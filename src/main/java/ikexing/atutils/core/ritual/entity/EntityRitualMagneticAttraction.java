package ikexing.atutils.core.ritual.entity;

import com.google.common.collect.Maps;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.ritual.RitualMagneticAttraction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

public class EntityRitualMagneticAttraction extends EntityRitualBase {

    private final RitualMagneticAttraction ritual;
    private final Map<BlockPos, String> posList = Maps.newHashMap();

    private final BlockPos posA;
    private final BlockPos posB;

    public EntityRitualMagneticAttraction(World worldIn) {
        super(worldIn);
        getDataManager().register(lifetime, ATUtils.ritualMa.getDuration() + 20);
        ritual = (RitualMagneticAttraction) ATUtils.ritualMa;
        posA = new BlockPos(posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z);
        posB = new BlockPos(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted % 20 == 0) {
            getBlockOre();
        }
        if (ticksExisted == ritual.getDuration() + 20) {

            if (world.isRemote) {

            } else {

            }
        }
    }

    private void getBlockOre() {
        posList.clear();
        Iterable<BlockPos> allInBox = BlockPos.getAllInBox(posA, posB);
        for (BlockPos inBox : allInBox) {
            IBlockState state = world.getBlockState(inBox);
            Block block = state.getBlock();
            ItemStack stack = block.getItem(world, inBox, state);

            for (ItemStack oreIron : OreDictionary.getOres("oreIron")) {
                if (oreIron.isItemEqual(stack)) {
                    posList.put(inBox, "Iron");
                }
            }
        }
    }
}
