package projectat.trutils.core.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WashingMachineCategory implements IRecipeCategory<WashingRecipeWrapper> {

    private final IDrawable background;
    private final IDrawable fluidOverlay;
    private final IDrawable progress;


    public WashingMachineCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation("trutils:textures/gui/jei_washing_machine.png");

        this.background = helper.drawableBuilder(location, 3, 3, 116, 65)
            .setTextureSize(128, 128)
            .build();
        this.fluidOverlay = helper.drawableBuilder(location, 13, 70, 7, 57)
            .setTextureSize(128, 128)
            .addPadding(3, 3, 9, 0)
            .build();
        this.progress = helper.drawableBuilder(location, 51, 70, 21, 13)
            .setTextureSize(128, 128)
            .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @NotNull @Override
    public String getUid() {
        return "trutils.washing_machine";
    }

    @NotNull @Override
    public String getTitle() {
        return I18n.format("tile.trutils.washing_machine.name");
    }

    @NotNull @Override
    public String getModName() {
        return "TRUtils";
    }

    @NotNull @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        progress.draw(minecraft, 51 - 3, 30 - 3);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WashingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.init(0, true, 1, 1, 16, 63, 4000, false, fluidOverlay);
        fluidStacks.init(1, false, 102 - 3, 1, 16, 63, 4000, false, fluidOverlay);
        fluidStacks.set(ingredients);

        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 23 - 3, 27 - 3);
        itemStacks.init(1, false, 81 - 3, 27 - 3);
        itemStacks.set(ingredients);
    }

}
