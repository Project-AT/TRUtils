package ikexing.atutils.botania.module;

import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModHydroangeas {

    public static List<HydroangeasHandler> handlerList = new ArrayList<>();

    public static class HydroangeasHandler {

        public static BlockLiquid fluidFactor;
        ILiquidStack liquidConsume;
        int manaGen;
        double factor;

        public HydroangeasHandler(ILiquidStack input, int manaGen, double factor) {
            this.liquidConsume = input;
            this.manaGen = manaGen;
            this.factor = factor;
        }

        public static void setFluidFactor(ILiquidStack inputFluid){
            fluidFactor = (BlockLiquid) getBlockLiquid(inputFluid);
        }

        public static BlockLiquid getBlockLiquid(ILiquidStack inputFluid) {
            return (BlockLiquid) CraftTweakerMC.getLiquidStack(inputFluid).getFluid().getBlock();
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
