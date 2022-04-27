package projectat.trutils.mixins.goodfeeling;

import projectat.trutils.core.goodfeeling.IGoodFeeling;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.api.recipe.RecipeElvenTrade;

@Mixin(value = RecipeElvenTrade.class, remap = false)
public class MixinRecipeElvenTrade implements IGoodFeeling {

    private int goodFeelingLevel;
    private double goodFeelingExperience;

    @Override
    public double getGoodFeelingExperience() {
        return this.goodFeelingExperience;
    }

    @Override
    public void setGoodFeelingExperience(double experience) {
        this.goodFeelingExperience = experience;
    }

    @Override
    public int getGoodFeelingLevel() {
        return goodFeelingLevel;
    }

    @Override
    public void setGoodFeelingLevel(int level) {
        this.goodFeelingLevel = level;
    }

}
