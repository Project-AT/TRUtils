package projectat.trutils.core.crt;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import projectat.trutils.Main;
import projectat.trutils.core.goodfeeling.IGoodFeeling;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;

@ZenRegister
@ZenClass("mods.trutils.IBotaniaAPI")
public class IBotaniaAPI {

    @ZenMethod
    public static void removeElvenTradeRecipe(IIngredient output) {
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                BotaniaAPI.elvenTradeRecipes.removeIf(recipe -> StackHelper.matches(output, CraftTweakerMC.getIItemStacks(recipe.getOutputs())));
            }

            @Override public String describe() {
                return "Removing ElvenTrade Recipe -> " + output.toCommandString();
            }
        });
    }

    @ZenMethod
    public static void addElvenTradeRecipe(String recipeName, int level, double experience, IIngredient[] input, IItemStack... outputs) {
        RecipeElvenTrade recipe = new RecipeElvenTrade(InputHelper.toStacks(outputs), InputHelper.toObjects(input));
        ((IGoodFeeling) recipe).setGoodFeelingLevel(level);
        ((IGoodFeeling) recipe).setGoodFeelingExperience(experience);
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                Main.RECIPE_ELVEN_TRADES.put(recipeName, recipe);
            }

            @Override public String describe() {
                return "Adding ElvenTrade Recipe -> " + recipeName;
            }
        });
    }

    @ZenMethod
    public static void addAlfPortalExperience(IIngredient input, double experience) {
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                Main.ALF_PORTAL_EXPERIENCE.put(input, experience);
            }

            @Override public String describe() {
                return "Adding AlfPortal Experience " + experience + "D -> " + input.toCommandString();
            }
        });
    }

}
