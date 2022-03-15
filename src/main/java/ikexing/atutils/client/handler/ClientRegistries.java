package ikexing.atutils.client.handler;

import ikexing.atutils.client.render.BlockOutlineRender;
import ikexing.atutils.core.fluids.FluidAura;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistries {

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        BlockOutlineRender.INSTANCE.init();
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
