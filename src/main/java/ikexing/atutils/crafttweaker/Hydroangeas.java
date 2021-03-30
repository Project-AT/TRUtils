package ikexing.atutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.atutils.Hydroangeas")
@ZenRegister
public class Hydroangeas {

    @ZenMethod
    public static void addManaRecipe(ILiquidStack inputFluid, int mana) {

    }

    private static String getFluidName(ILiquidStack stack) {
        return stack.getDefinition().getName();
    }

}
