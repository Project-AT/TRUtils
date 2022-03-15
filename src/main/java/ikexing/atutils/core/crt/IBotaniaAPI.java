package ikexing.atutils.core.crt;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import ikexing.atutils.core.goodfeeling.IGoodFeeling;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

@ZenRegister
@ZenClass("mods.atutils.IBotaniaAPI")
public class IBotaniaAPI {

    @ZenMethod
    public static void registerElvenTradeRecipe(String recipeName, IItemStack[] outputs, IIngredient[] input, int level) {
        RecipeElvenTrade recipe = new RecipeElvenTrade(InputHelper.toStacks(outputs), InputHelper.toObjects(input));
        ((IGoodFeeling) recipe).setGoodFeeling(level);
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                BotaniaAPI.elvenTradeRecipes.add(recipe);
            }

            @Override public String describe() {
                return "Adding ElvenTrade Recipe -> " + recipeName;
            }
        });
    }
    
}
