package ikexing.atutils.botania.module;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.BlockLiquid;

import java.util.ArrayList;
import java.util.List;

public class ModHydroangeas {

    public static List<HydroangeasHandler> handlerList = new ArrayList<>();

    public static class HydroangeasHandler {

        ILiquidStack liquidConsume;
        int manaGen;
        double factor;

        public HydroangeasHandler(ILiquidStack input, int manaGen, double factor) {
            this.liquidConsume = input;
            this.manaGen = manaGen;
            this.factor = factor;
        }

        public BlockLiquid getBlockLiquid() {
            return (BlockLiquid) CraftTweakerMC.getLiquidStack(liquidConsume).getFluid().getBlock();
        }

        public int getManaGen() {
            return this.manaGen;
        }

        public double getManaFactor() {
            return this.factor;
        }

    }

}
