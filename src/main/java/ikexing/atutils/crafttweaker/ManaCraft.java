package ikexing.atutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.atutils.ManaCraft")
@ZenRegister
public class ManaCraft {

    public static Block block;

    @ZenMethod
    public static void setBlock(IBlock block){
        ManaCraft.block = CraftTweakerMC.getBlock(block);
    }
}
