package ikexing.atutils.core.crt;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.goodfeeling.IGoodFeeling;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.recipe.RecipeElvenTrade;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass("mods.atutils.IBotaniaAPI")
public class IBotaniaAPI {

    @ZenMethod
    public static void registerElvenTradeRecipe(String recipeName, int level, IIngredient[] input, IItemStack... outputs) {
        RecipeElvenTrade recipe = new RecipeElvenTrade(InputHelper.toStacks(outputs), InputHelper.toObjects(input));
        ((IGoodFeeling) recipe).setGoodFeeling(level);
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
    public static int getElvenTradeRecipeLevel(IItemStack[] input) {
        List<ItemStack> collect = Arrays.stream(CraftTweakerMC.getItemStacks(input)).collect(Collectors.toList());
        int toReturn = -1;
        for (RecipeElvenTrade value : ATUtils.RECIPE_ELVEN_TRADES.values()) {
            int goodFeeling = ((IGoodFeeling) value).getGoodFeeling();
            if (value.matches(collect, false) && goodFeeling >= toReturn) {
                toReturn = goodFeeling;
            }
        }
        return toReturn;
    }

}
