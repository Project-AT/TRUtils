package ikexing.atutils.client.render;


import com.google.gson.JsonSyntaxException;
import ikexing.atutils.ATUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class BlockOutlineRender {
    public static final BlockOutlineRender INSTANCE = new BlockOutlineRender();

    private BlockOutlineRender() {

    }

    private static final Logger LOGGER = LogManager.getLogger();

    private Framebuffer outlineBuffer;
    private ShaderGroup shader;
    private Minecraft mc;

    private int displayWidth = -1;
    private int displayHeight = -1;

    public WeakHashMap<Object, BlockPos> getPositionProviders() {
        return positionProviders;
    }

    private WeakHashMap<Object, BlockPos> positionProviders = new WeakHashMap<>();

    public void init() {
        if (!OpenGlHelper.shadersSupported) {
            return;
        }


        //这里idea会提示始终非空，但是实际上它有可能null
        //noinspection ConstantConditions
        if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }

        ResourceLocation resourcelocation = new ResourceLocation(ATUtils.MODID, "shaders/post/block_outline.json");

        try {
            this.mc = Minecraft.getMinecraft();
            //初始化着色器
            this.shader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);

            //初始化帧缓冲
            this.displayWidth = mc.displayWidth;
            this.displayHeight = mc.displayHeight;
            this.shader.createBindFramebuffers(displayWidth, displayHeight);
            this.outlineBuffer = this.shader.getFramebufferRaw("atutils:final");
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

        this.outlineBuffer.bindFramebuffer(false);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);


        GlStateManager.pushMatrix();

        //坐标根据玩家镜头一次变换
        RenderManager renderManager = mc.getRenderManager();
        double viewerPosX = renderManager.viewerPosX;
        double viewerPosY = renderManager.viewerPosY;
        double viewerPosZ = renderManager.viewerPosZ;
        GlStateManager.translate(-viewerPosX, -viewerPosY, -viewerPosZ);


        BlockRendererDispatcher renderer = mc.getBlockRendererDispatcher();
        GlStateManager.color(1F, 1F, 1F, 1F);

        for (BlockPos pos : positionProviders.values()) {
            if (!mc.world.isBlockLoaded(pos)) {
                continue;
            }
            IBlockState state = mc.world.getBlockState(pos);
            GlStateManager.pushMatrix();
            //渲染具体坐标二次变换
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            IBakedModel ibakedmodel = renderer.getModelForState(state);
            renderer.getBlockModelRenderer().renderModelBrightnessColor(
                state, ibakedmodel, 1.0f, 1.0f, 1.0f, 1.0f

            );
            GlStateManager.popMatrix();
        }


        GlStateManager.popMatrix();

        //着色器后处理轮廓
        this.shader.render(partialTicks);

        GlStateManager.popAttrib();


        //改回主缓冲区
        this.mc.getFramebuffer().bindFramebuffer(false);
    }

    /**
     * 将缓冲区内容渲染到屏幕上面
     */
    public void renderToScreen() {
        if (this.isEnabled()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            this.outlineBuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }


    public boolean isEnabled() {
        return this.outlineBuffer != null && this.shader != null && this.mc != null && this.mc.player != null;
    }

    public boolean hasSomethingToRender() {
        int size = 0;
        for (BlockPos pos : this.positionProviders.values()) {
            size++;
        }
        return size > 0;
    }
}

