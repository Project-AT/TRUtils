package ikexing.atutils.client.render;


import com.google.gson.JsonSyntaxException;
import ikexing.atutils.ATUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockOutlineRender {

    private static final Logger LOGGER = LogManager.getLogger();

    private Framebuffer outlineBuffer;
    private ShaderGroup shader;
    private Minecraft mc;

    private int displayWidth = -1;
    private int displayHeight = -1;

    private Map<BlockPos, IBlockState> list;


    public void init() {
        if (!OpenGlHelper.shadersSupported) {
            return;
        }

        ResourceLocation resourcelocation = new ResourceLocation(ATUtils.MODID, "shaders/post/block_outline.json");

        try {
            this.shader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);

            this.mc = Minecraft.getMinecraft();
            this.displayWidth = mc.displayWidth;
            this.displayHeight = mc.displayHeight;
            this.shader.createBindFramebuffers(displayWidth, displayHeight);
            this.outlineBuffer = this.shader.getFramebufferRaw("final");
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.warn("Failed to load shader: {}", resourcelocation, e);
        }

    }


    public void updateFrameBufferSize() {

        if (!isEnabled()) {
            return;
        }

        if (this.displayHeight != mc.displayHeight || this.displayWidth != mc.displayWidth) {
            this.displayWidth = mc.displayWidth;
            this.displayHeight = mc.displayHeight;
            this.shader.createBindFramebuffers(displayWidth, displayHeight);
        }

    }

    public void renderToBuffer(float partialTicks) {
        if (!isEnabled()) {
            return;
        }
        this.outlineBuffer.framebufferClear();

        if (list != null && !list.isEmpty()) {
            this.outlineBuffer.bindFramebuffer(false);
            //关掉一吨东西，因为都不需要（（
            GlStateManager.disableAlpha();
            GlStateManager.disableDepth();
            GlStateManager.disableColorMaterial();
            GlStateManager.depthFunc(GL11.GL_ALWAYS);
            GlStateManager.disableFog();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();

            RenderHelper.disableStandardItemLighting();

            BlockRendererDispatcher renderer = mc.getBlockRendererDispatcher();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            for (Map.Entry<BlockPos, IBlockState> entry : list.entrySet()) {
                renderer.renderBlock(entry.getValue(), entry.getKey(), mc.world, bufferbuilder);
            }
            bufferbuilder.finishDrawing();

            RenderHelper.enableStandardItemLighting();


            GlStateManager.depthMask(false);
            this.shader.render(partialTicks);
            GlStateManager.depthMask(true);


            GlStateManager.enableLighting();
            GlStateManager.enableBlend();
            GlStateManager.enableFog();
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
            GlStateManager.enableColorMaterial();
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();

        }
        this.mc.getFramebuffer().bindFramebuffer(false);

    }

    public void renderToScreen() {
        if (this.isEnabled()) {
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            this.outlineBuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }


    public boolean isEnabled() {
        return this.outlineBuffer != null && this.shader != null && this.mc != null && this.mc.player != null;
    }

    public void setRenderList(Map<BlockPos, IBlockState> list) {
        this.list = list;
    }
}

