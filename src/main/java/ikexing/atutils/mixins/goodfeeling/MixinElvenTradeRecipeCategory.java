package ikexing.atutils.mixins.goodfeeling;

import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeCategory;

@Mixin(value = ElvenTradeRecipeCategory.class, remap = false)
public abstract class MixinElvenTradeRecipeCategory {



}
