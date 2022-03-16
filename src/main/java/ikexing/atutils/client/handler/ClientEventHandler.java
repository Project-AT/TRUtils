package ikexing.atutils.client.handler;

import ikexing.atutils.ATUtils;
import ikexing.atutils.client.render.BlockOutlineRender;
import ikexing.atutils.client.utils.OpenGLdebugging;
import ikexing.atutils.core.ritual.entity.EntityRitualMagneticAttraction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.ShaderHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
}
