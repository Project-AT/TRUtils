package ikexing.atutils.core.crt;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.goodfeeling.IGoodFeeling;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

@ZenRegister
@ZenClass("mods.atutils.IBotaniaAPI")
public class IBotaniaAPI {

    @ZenMethod
    public static void registerElvenTradeRecipe(String recipeName, int level, IIngredient[] input, IItemStack... outputs) {
        RecipeElvenTrade recipe = new RecipeElvenTrade(InputHelper.toStacks(outputs), InputHelper.toObjects(input));
        ((IGoodFeeling) recipe).setGoodFeeling(level);
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                BotaniaAPI.elvenTradeRecipes.add(recipe);
                ATUtils.RECIPE_ELVEN_TRADES.put(recipeName, recipe);
            }

            @Override public String describe() {
                return "Adding ElvenTrade Recipe -> " + recipeName;
            }
        });
    }

    @ZenMethod
    public static int getElvenTradeRecipeLevel(String recipeName) {
        return ((IGoodFeeling) ATUtils.RECIPE_ELVEN_TRADES.get(recipeName)).getGoodFeeling();
    }

}
