package ikexing.atutils.core.events;

import ikexing.atutils.ATUtils;
import ikexing.atutils.core.fluids.FluidAura;
import ikexing.atutils.core.fluids.FluidHandlerAuraBottle;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEvent {


    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == de.ellpeck.naturesaura.items.ModItems.AURA_BOTTLE ||
            event.getObject().getItem() == de.ellpeck.naturesaura.items.ModItems.BOTTLE_TWO
        ) {
            event.addCapability(new ResourceLocation(ATUtils.MODID, "aura_bottle"), new FluidHandlerAuraBottle(event.getObject()));

        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.getMap();
        textureMap.registerSprite(FluidAura.auraEnd.getFlowing());
        textureMap.registerSprite(FluidAura.auraNether.getFlowing());
        textureMap.registerSprite(FluidAura.auraOverworld.getFlowing());
        textureMap.registerSprite(FluidAura.auraUnderworld.getFlowing());
        textureMap.registerSprite(FluidAura.auraEnd.getStill());
        textureMap.registerSprite(FluidAura.auraNether.getStill());
        textureMap.registerSprite(FluidAura.auraOverworld.getStill());
        textureMap.registerSprite(FluidAura.auraUnderworld.getStill());

    }
}
