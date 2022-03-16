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
            int width = mc.displayWidth;
            int height = mc.displayHeight;

            GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            //从 FrameBuffer.renderBufferExt方法里面复制出来，并且将所有GlStateManager的调用替换成了原始的opengl调用

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDepthMask(false);
            GL11.glColorMask(true, true, true, false);

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);


            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);

            this.outlineBuffer.bindFramebufferTexture();
            float u = (float) this.outlineBuffer.framebufferWidth / (float) this.outlineBuffer.framebufferTextureWidth;
            float v = (float) this.outlineBuffer.framebufferHeight / (float) this.outlineBuffer.framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0, height, 0).tex(0, 0).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(width, height, 0).tex(u, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(width, 0, 0).tex(u, v).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(0, 0, 0).tex(0.0D, v).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.outlineBuffer.unbindFramebufferTexture();

            GL11.glPopMatrix();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            GL11.glPopAttrib();
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

