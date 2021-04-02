package ikexing.atutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ikexing.atutils.botania.module.ModHydroangeas;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.atutils.Hydroangeas")
@ZenRegister
public class Hydroangeas {

    @ZenMethod
    public static void addManaRecipe(ILiquidStack inputFluid, int mana, double factor) {
        ModHydroangeas.handlerList.add(new ModHydroangeas.HydroangeasHandler(inputFluid, mana, factor));
    }

    // mana factor is 2.0 by default
    @ZenMethod
    public static void addManaRecipe(ILiquidStack inputFluid, int mana) {
        addManaRecipe(inputFluid, mana, 2.0D);
    }

    @ZenMethod
    public static void setFactor(ILiquidStack inputFluid){
        ModHydroangeas.HydroangeasHandler.setFluidFactor(inputFluid);
    }


}
