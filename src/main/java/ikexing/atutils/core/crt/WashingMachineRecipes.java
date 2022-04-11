package ikexing.atutils.core.crt;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.atutils.WashingMachineRecipes")
public class WashingMachineRecipes {

    public static final List<WashingMachineRecipe> recipes = new ArrayList<>();

    @ZenMethod
    public static void addRecipe(int recipeTime, int energyConsume, IIngredient fluidInput, IIngredient itemInput,
            IItemStack itemOutput, ILiquidStack fluidOutput) {
        // 使用IAction接口保证不会重复实例化
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                recipes.add(new WashingMachineRecipe(recipeTime, energyConsume, fluidInput, itemInput, itemOutput, fluidOutput));
            }

            @Override public String describe() {
                return null;
            }
        });
    }

    @ZenMethod
    public static void clear() {
        recipes.clear();
    }

    @ZenMethod
    public static List<WashingMachineRecipe> getAllRecipes() {
        return WashingMachineRecipes.recipes;
    }

}
