package ikexing.atutils.botania.module;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ModOrechid {
    public static Map<ItemStack, ItemStack> OrechidMap = new HashMap();

    public static void addRecipe(IItemStack input, IItemStack output){
        if(output.isItemBlock()){
            OrechidMap.put(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output));
        }else{
            CraftTweakerAPI.getLogger().logError("input or output is not a block.");
        }

    }
}
