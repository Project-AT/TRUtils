package ikexing.atutils.core.events;

import ikexing.atutils.ATUtils;
import ikexing.atutils.core.fluids.FluidHandlerAuraBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEvent {


    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().getItem() == de.ellpeck.naturesaura.items.ModItems.AURA_BOTTLE ||
            event.getObject().getItem() == de.ellpeck.naturesaura.items.ModItems.BOTTLE_TWO
        ) {
            event.addCapability(new ResourceLocation(ATUtils.MODID, "aura_bottle"), new FluidHandlerAuraBottle(event.getObject()));

        }
    }
}
