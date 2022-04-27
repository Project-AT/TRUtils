package projectat.trutils.core.jei;

import com.google.common.collect.ImmutableList;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import projectat.trutils.core.crt.WashingMachineRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@ParametersAreNonnullByDefault
public class WashingRecipeWrapper implements IRecipeWrapper {
    private final WashingMachineRecipe recipe;

    public WashingRecipeWrapper(WashingMachineRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ImmutableList.Builder<FluidStack> fluidInputs = ImmutableList.builder();
        for (ILiquidStack liquid : recipe.getFluidInput().getLiquids()) {
            fluidInputs.add(CraftTweakerMC.getLiquidStack(liquid));
        }
        ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(fluidInputs.build()));

        ImmutableList.Builder<ItemStack> itemInputs = ImmutableList.builder();
        for (IItemStack item : recipe.getItemInput().getItems()) {
            itemInputs.add(CraftTweakerMC.getItemStack(item));
        }
        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(itemInputs.build()));

        ingredients.setOutput(VanillaTypes.FLUID, CraftTweakerMC.getLiquidStack(recipe.getFluidOutput()));
        ingredients.setOutput(VanillaTypes.ITEM, CraftTweakerMC.getItemStack(recipe.getItemOutput()));

    }
}
