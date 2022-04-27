package projectat.trutils.core.jei;

import projectat.trutils.core.block.BlockWashingMachine;
import projectat.trutils.core.crt.WashingMachineRecipe;
import projectat.trutils.core.crt.WashingMachineRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ATPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new WashingMachineCategory(registry.getJeiHelpers().getGuiHelper()));


    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockWashingMachine.ITEM_BLOCK), "trutils.washing_machine");
        registry.handleRecipes(WashingMachineRecipe.class, WashingRecipeWrapper::new, "trutils.washing_machine");
        registry.addRecipes(WashingMachineRecipes.getAllRecipes(), "trutils.washing_machine");

    }
}
