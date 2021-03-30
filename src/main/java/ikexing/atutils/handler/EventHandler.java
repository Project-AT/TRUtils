package ikexing.atutils.handler;

import ikexing.atutils.ATUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = ATUtils.MODID)
public class EventHandler {

    @SubscribeEvent
    public void teleportToTF(TickEvent.PlayerTickEvent event) {
        if (Loader.isModLoaded("twilightforest")) {
            EntityPlayer player = event.player;
        }
    }

}
