package ikexing.atutils.mixins.goodfeeling;

import com.google.common.collect.ImmutableList;
import ikexing.atutils.core.goodfeeling.IGoodFeeling;
import ikexing.atutils.core.item.GoodFeelingLevel;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeWrapper;

import java.util.List;

@Mixin(value = ElvenTradeRecipeWrapper.class, remap = false)
public abstract class MixinElvenTradeRecipeWrapper implements IGoodFeeling, IRecipeWrapper {

    private int goodFeeling;

    @Mutable @Final @Shadow
    private List<List<ItemStack>> input;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RecipeElvenTrade recipe, CallbackInfo ci) {
        goodFeeling = ((IGoodFeeling) recipe).getGoodFeelingLevel();
        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
        for (Object o : recipe.getInputs()) {
            if (o instanceof ItemStack) {
                builder.add(ImmutableList.of((ItemStack) o));
            }
            if (o instanceof String) {
                builder.add(OreDictionary.getOres((String) o));
            }
        }
        builder.add(ImmutableList.of(new ItemStack(GoodFeelingLevel.INSTANCE)));
        input = builder.build();
    }

    @Override
    public int getGoodFeelingLevel() {
        return this.goodFeeling;
    }

    @Override
    public void setGoodFeelingLevel(int level) {
        this.goodFeeling = level;
    }

    @Override
    public double getGoodFeelingExperience() {
        return 0;
    }

    @Override
    public void setGoodFeelingExperience(double experience) {}

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        IRecipeWrapper.super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
        int posX = 42 + (18 * input.size()) - 4;
        minecraft.getRenderManager().getFontRenderer().drawString(String.valueOf(getGoodFeelingLevel()), posX, 10, 0xFFFFFF);
    }
}
