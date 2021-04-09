package ikexing.atutils.client.jei;

import ikexing.atutils.botania.module.ModHydroangeas;
import ikexing.atutils.client.jei.hydroangeas.HydroangeasRecipeCategory;
import ikexing.atutils.client.jei.hydroangeas.HydroangeasRecipeWrapper;
import mezz.jei.Internal;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.gui.GuiHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JEIPlugin
public class JeiATPlugin implements IModPlugin {

    List<HydroangeasRecipeWrapper> recipes = new ArrayList<>();

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        GuiHelper guiHelper = Internal.getHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new HydroangeasRecipeCategory(guiHelper)
        );
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS), HydroangeasRecipeCategory.UID);

        for (Map.Entry<Block, Double> entry : ModHydroangeas.blockFactorList.entrySet()) {
            for (ModHydroangeas.HydroangeasHandler handler : ModHydroangeas.handlerList) {
                recipes.add(new HydroangeasRecipeWrapper(entry, handler));
            }
        }
        registry.addRecipes(recipes, HydroangeasRecipeCategory.UID);

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
    }

}
