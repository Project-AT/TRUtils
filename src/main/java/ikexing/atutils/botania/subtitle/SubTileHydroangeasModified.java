package ikexing.atutils.botania.subtitle;

import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;

public class SubTileHydroangeasModified extends SubTileGenerating {

    @Override
    public void onUpdate() {
        super.onUpdate();
        BlockPos pos = supertile.getPos();

        for(BlockPos bm : BlockPos.getAllInBox(
                pos.add(-1, 0, -1),
                pos.add(1, 0, 1))){

            System.out.println("test");
        }

    }
}
