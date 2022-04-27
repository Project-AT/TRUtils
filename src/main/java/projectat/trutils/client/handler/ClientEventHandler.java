package projectat.trutils.client.handler;

import projectat.trutils.client.render.BlockOutlineRender;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        if (!BlockOutlineRender.INSTANCE.hasSomethingToRender()) {
            return;
        }

        BlockOutlineRender.INSTANCE.updateFrameBufferSize();
        BlockOutlineRender.INSTANCE.renderToBuffer(event.getPartialTicks());
        BlockOutlineRender.INSTANCE.renderToScreen();
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
//        if(event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
//            return;
//        }
//        if (!BlockOutlineRender.INSTANCE.hasSomethingToRender()) {
//            return;
//        }
//        BlockOutlineRender.INSTANCE.renderToScreen();
    }

}
