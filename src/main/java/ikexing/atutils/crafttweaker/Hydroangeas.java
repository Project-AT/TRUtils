package ikexing.atutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import ikexing.atutils.botania.module.ModHydroangeas;
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
    public static void setFactor(ILiquidStack inputFluid) {
        ModHydroangeas.setFluidFactor(inputFluid);
    }

    @ZenMethod
    public static void setBlockBelowFactor(IItemStack block, double factor) {
        ModHydroangeas.setBlockBelowFactor(block, factor);
    }

    // mana factor is 2.0 by default
    @ZenMethod
    public static void setBlockBelowFactor(IItemStack block) {
        setBlockBelowFactor(block, 2.0D);
    }

}
