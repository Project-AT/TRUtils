package ikexing.atutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import ikexing.atutils.botania.module.ModOrechid;
import crafttweaker.api.block.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.atutils.Orechid")
@ZenRegister
public class Orechid {
    @ZenMethod
    public static void addOrechidRecipe(IItemStack input, IItemStack output){

        ModOrechid.addRecipe(input, output);
    }
}
