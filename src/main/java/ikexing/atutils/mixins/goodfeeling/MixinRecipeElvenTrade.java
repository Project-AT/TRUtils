package ikexing.atutils.mixins.goodfeeling;

import ikexing.atutils.core.goodfeeling.IGoodFeeling;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.api.recipe.RecipeElvenTrade;

@Mixin(value = RecipeElvenTrade.class, remap = false)
public class MixinRecipeElvenTrade implements IGoodFeeling {

    private int goodFeeling;

    @Override
    public int getGoodFeeling() {
        return goodFeeling;
    }

    @Override
    public void setGoodFeeling(int goodFeeling) {
        this.goodFeeling = goodFeeling;
    }

}
