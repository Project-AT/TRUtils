package ikexing.atutils.core.jei;

import ikexing.atutils.core.block.BlockWashingMachine;
import ikexing.atutils.core.crt.WashingMachineRecipe;
import ikexing.atutils.core.crt.WashingMachineRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ATPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new WashingMachineCategory(registry.getJeiHelpers().getGuiHelper()));


    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockWashingMachine.ITEM_BLOCK), "atutils.washing_machine");
        registry.handleRecipes(WashingMachineRecipe.class, WashingRecipeWrapper::new, "atutils.washing_machine");
        registry.addRecipes(WashingMachineRecipes.getAllRecipes(), "atutils.washing_machine");

    }
}
