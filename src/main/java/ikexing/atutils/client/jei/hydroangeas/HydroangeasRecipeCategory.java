package ikexing.atutils.client.jei.hydroangeas;

import ikexing.atutils.ATUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nullable;

public class HydroangeasRecipeCategory implements IRecipeCategory<HydroangeasRecipeWrapper> {

    public static final String UID = "atutils.hydroangeas";
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public HydroangeasRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(128, 72);
        icon = guiHelper.createDrawableIngredient(ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS));
        localizedName = I18n.format("atutils.jei.hydroangeas");
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return ATUtils.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, HydroangeasRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 12, 12);
        recipeLayout.getItemStacks().set(0, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS));

        recipeLayout.getFluidStacks().init(1, true, 100, 12, 16, 16, 1000, false, null);
        recipeLayout.getFluidStacks().set(1, ingredients.getInputs(VanillaTypes.FLUID).get(0));

        recipeLayout.getItemStacks().init(2, true, 12, 36);
        recipeLayout.getItemStacks().set(2, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        recipeLayout.getFluidStacks().init(3, true, 100, 36, 16, 16, 1000, false, null);
        recipeLayout.getFluidStacks().set(3, ingredients.getInputs(VanillaTypes.FLUID).get(1));

    }

}
