package ikexing.atutils.core.crt;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.atutils.WashingMachineRecipes")
public class WashingMachineRecipes {

    @ZenProperty("recipes")
    public static final List<WashingMachineRecipe> recipes = new ArrayList<>();

    @ZenMethod("addRecipe")
    public static void addRecipe(int recipeTime, int energyConsume, IIngredient fluidInput, IIngredient itemInput, IItemStack itemOutput, ILiquidStack fluidOutput) {
        recipes.add(new WashingMachineRecipe(recipeTime, energyConsume, fluidInput, itemInput, itemOutput, fluidOutput));
    }

    @ZenMethod("clear")
    public static void clear() {
        recipes.clear();
    }
}
