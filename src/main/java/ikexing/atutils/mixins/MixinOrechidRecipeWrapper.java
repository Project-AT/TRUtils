package ikexing.atutils.mixins;

import ikexing.atutils.botania.module.ModOrechid;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.client.integration.jei.orechid.OrechidRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static ikexing.atutils.botania.module.ModOrechid.OrechidMap;

@Mixin(value = OrechidRecipeWrapper.class, remap = false)
public class MixinOrechidRecipeWrapper {
    /**
     * @author ikexing
     * @reason just two line of code...
     */
    @Overwrite
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<ItemStack> output = new ArrayList();
        List<ItemStack> input = new ArrayList(ModOrechid.inputList);

        for (Entry<IBlockState, ItemStack> entry : OrechidMap.entrySet()){
            output.add(entry.getValue());
        }

        System.out.println("test jei");
        System.out.println(ModOrechid.inputList);
        System.out.println(output);
        ingredients.setInputs(VanillaTypes.ITEM, input);
        ingredients.setOutputs(VanillaTypes.ITEM, output);

    }
}
