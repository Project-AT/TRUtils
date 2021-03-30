package ikexing.atutils.botania.module;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.BlockLiquid;

import java.util.ArrayList;
import java.util.List;

public class ModHydroangeas {

    public static List<HydroangeasHandler> handlerList = new ArrayList<>();

    public class HydroangeasHandler {

        ILiquidStack liquidConsume;
        int manaGen;

        public BlockLiquid getBlockLiquid() {
            return (BlockLiquid) CraftTweakerMC.getLiquidStack(liquidConsume).getFluid().getBlock();
        }

        public int getManaGen() {
            return manaGen;
        }

    }

    public static void init() {

    }

}
