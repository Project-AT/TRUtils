package ikexing.atutils.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import ikexing.atutils.twilight.ATTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import twilightforest.TFConfig;


@ZenClass("mods.atutils.TwilightTeleporter")
@ZenRegister
public class TwilightTeleporter{
    @ZenMethod
    public static void teleportPlayer(IPlayer playerIn) {
        int destination = TFConfig.dimension.dimensionID;

        EntityPlayer player = CraftTweakerMC.getPlayer(playerIn);
        if (player.isDead || player.world.isRemote) {
            return;
        }

        if (player instanceof EntityPlayerMP) {
            player.changeDimension(destination, ATTeleporter.getTeleporterForDim(((EntityPlayerMP) player).mcServer, destination));
        }
    }
}
