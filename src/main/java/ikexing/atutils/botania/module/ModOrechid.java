package ikexing.atutils.botania.module;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModOrechid {
    public static Map<IBlockState, ItemStack> OrechidMap = new HashMap();
    public static List<ItemStack> inputList = new ArrayList();

    public static void addRecipe(IItemStack input, IItemStack output){
        if(output.isItemBlock()){
            OrechidMap.put(CraftTweakerMC.getBlockState(input.asBlock().getDefinition().getStateFromMeta(input.getMetadata())), CraftTweakerMC.getItemStack(output));
            inputList.add(CraftTweakerMC.getItemStack(input));
        }else{
            CraftTweakerAPI.getLogger().logError("output or input is not a block.");
        }

    }
}
