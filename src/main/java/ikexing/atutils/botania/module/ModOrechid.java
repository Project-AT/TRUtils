package ikexing.atutils.botania.module;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;


import java.util.*;

public class ModOrechid {
    public static Map<IBlockState, ItemStack> OrechidMap = new HashMap();
    public static Map<ItemStack, ItemStack> OrechidItemMap = new HashMap();

    public static void addRecipe(IItemStack input, IItemStack output){
        if(output.isItemBlock() || input.isItemBlock()){
            OrechidMap.put(CraftTweakerMC.getBlockState(input.asBlock().getDefinition().getStateFromMeta(input.getMetadata())), CraftTweakerMC.getItemStack(output));
            OrechidItemMap.put(CraftTweakerMC.getItemStack(input),CraftTweakerMC.getItemStack(output));
        }else{
            CraftTweakerAPI.getLogger().logError("input or output is not a block.");
        }

    }
}
