package ikexing.atutils.mixins;

import com.google.common.collect.Lists;
import ikexing.atutils.ATUtils;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import primal_tech.ModBlocks;
import primal_tech.jei.JEIPlugInStoneGrill;
import primal_tech.jei.stone_grill.GrillRecipeWrapper;

import java.util.List;
import java.util.Map;

@Pseudo
@Mixin(value = JEIPlugInStoneGrill.class, remap = false)
public class MixinJEIPlugInStoneGrill {

    @Inject(method = "getFurnaceRecipes",
            at = @At(value = "HEAD"), cancellable = true)
    public void getFurnaceRecipes(List<GrillRecipeWrapper> recipes,
            IJeiHelpers helpers,
            CallbackInfoReturnable<List<GrillRecipeWrapper>> cir) {
        IStackHelper stackHelper = helpers.getStackHelper();
        FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();
        for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {
            List<ItemStack> output = Lists.newArrayList();
            ItemStack inStack = entry.getKey();
            ItemStack outStack = entry.getValue();
            ItemStack outStack2 = new ItemStack(ModBlocks.STONE_GRILL);

            if (ATUtils.isCancel(outStack.copy())) continue;

            List<ItemStack> inputs = stackHelper.getSubtypes(inStack);
            output.add(outStack);
            output.add(outStack2);
            recipes.add(new GrillRecipeWrapper(inputs, output));
        }
        cir.setReturnValue(recipes);
    }

}
