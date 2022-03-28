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
import vazkii.botania.api.recipe.RecipeElvenTrade;

import java.util.Map;

@ZenRegister
@ZenClass("mods.atutils.IBotaniaAPI")
public class IBotaniaAPI {

    @ZenMethod
    public static void addElvenTradeRecipe(String recipeName, int level, double experience, IIngredient[] input, IItemStack... outputs) {
        RecipeElvenTrade recipe = new RecipeElvenTrade(InputHelper.toStacks(outputs), InputHelper.toObjects(input));
        ((IGoodFeeling) recipe).setGoodFeelingLevel(level);
        ((IGoodFeeling) recipe).setGoodFeelingExperience(experience);
        CraftTweakerAPI.apply(new IAction() {
            @Override public void apply() {
                ATUtils.RECIPE_ELVEN_TRADES.put(recipeName, recipe);
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
                ATUtils.ALF_PORTAL_EXPERIENCE.put(input, experience);
            }

            @Override public String describe() {
                return "Adding AlfPortal Experience " + experience + "D -> " + input.toCommandString();
            }
        });
    }

    @ZenMethod
    public static Map<IIngredient, Double> getAllAlfPortalExperience() {
        return ATUtils.ALF_PORTAL_EXPERIENCE;
    }

}
