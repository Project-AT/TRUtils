package ikexing.atutils.client.tesr;

import ikexing.atutils.core.tile.TileWashingMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class TileWashingMachineRender extends TileEntitySpecialRenderer<TileWashingMachine> {

    private static final AxisAlignedBB[] BOUNDS_IN = new AxisAlignedBB[4];
    private static final AxisAlignedBB[] BOUNDS_OUT = new AxisAlignedBB[4];

    static {
        AxisAlignedBB TANK_BOUNDS_1 = new AxisAlignedBB(9 / 16f, 1.5 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 7 / 16f);
        AxisAlignedBB TANK_BOUNDS_2 = new AxisAlignedBB(1 / 16f, 1.5 / 16f, 1 / 16f, 6 / 16f, 15 / 16f, 7 / 16f);
        for (int i = 0; i < 4; i++) {
            BOUNDS_IN[i] = rotateAABB(TANK_BOUNDS_1, i);
            BOUNDS_OUT[i] = rotateAABB(TANK_BOUNDS_2, i);
        }
    }


    public static AxisAlignedBB rotateAABB(AxisAlignedBB in, int rot) {
        switch (rot) {
            case 1:
                return new AxisAlignedBB(in.minZ, in.minY, 1 - in.minX, in.maxZ, in.maxY, 1 - in.maxX);
            case 2:
                return in;
            case 3:
                return new AxisAlignedBB(1 - in.minZ, in.minY, in.minX, 1 - in.maxZ, in.maxY, in.maxX);
            case 0:
                return new AxisAlignedBB(1 - in.minX, in.minY, 1 - in.minZ, 1 - in.maxX, in.maxY, 1 - in.maxZ);
            default:
                throw new IllegalArgumentException("rot must 0, 1, 2, 3");
        }
    }

    @Override
    public void render(@Nullable TileWashingMachine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te == null) {
            return;
        }
        float inputPercent = te.getInputTank().getPercentage(partialTicks);
        float outputPercent = te.getOutputTank().getPercentage(partialTicks);

        FluidStack input = te.getInputTank().getLastFluid();
        FluidStack output = te.getOutputTank().getLastFluid();

        GlStateManager.pushMatrix();
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translate(x, y, z);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);


        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        EnumFacing direction = te.getDirection();
        AxisAlignedBB inputBounds = BOUNDS_IN[direction.getHorizontalIndex()];
        AxisAlignedBB outputBounds = BOUNDS_OUT[direction.getHorizontalIndex()];
        BlockPos facingPos = te.getPos().offset(direction);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);


        if (input != null)
            renderFluid(buffer, input, facingPos, direction, inputBounds,
                inputPercent, input.getFluid().isGaseous());

        if (output != null)
            renderFluid(buffer, output, facingPos, direction, outputBounds,
                outputPercent, output.getFluid().isGaseous());

        tess.draw();

        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();


        GlStateManager.popMatrix();

    }


    /**
     * Render fluid
     */
    private void renderFluid(BufferBuilder buffer, FluidStack fluidStack, BlockPos pos, EnumFacing facing, AxisAlignedBB bounds, float percentage, boolean isGas) {

        Fluid fluid = fluidStack.getFluid();

        int c = fluid.getColor(fluidStack);
        int b = c & 255;
        int g = c >> 8 & 255;
        int r = c >> 16 & 255;
        int a = c >> 24 & 255;

        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill(fluidStack).toString());


        double tankHeight = bounds.maxY - bounds.minY;
        double minY = bounds.minY, maxY = (bounds.minY + (tankHeight * percentage));
        if (isGas) {
            double yOff = bounds.maxY - maxY;  // lighter than air fluids move to the top of the tank
            minY += yOff;
            maxY += yOff;
        }


        double minU = sprite.getMinU();
        double maxU = sprite.getMaxU();
        double minV = sprite.getMinV();
        double maxV = sprite.getMaxV();


        int i = this.getWorld().getCombinedLight(pos, fluid.getLuminosity(fluidStack));
        int skyLight = i >> 16 & 0xFFFF;
        int blockLight = i & 0xFFFF;
//
//        //Bottom
//        buffer.pos(bounds.minX, minY, bounds.maxZ).tex(minU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
//        buffer.pos(bounds.minX, minY, bounds.minZ).tex(minU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
//        buffer.pos(bounds.maxX, minY, bounds.minZ).tex(maxU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
//        buffer.pos(bounds.maxX, minY, bounds.maxZ).tex(maxU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();

        //Top
        buffer.pos(bounds.minX, maxY, bounds.maxZ).tex(minU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
        buffer.pos(bounds.maxX, maxY, bounds.maxZ).tex(maxU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
        buffer.pos(bounds.maxX, maxY, bounds.minZ).tex(maxU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
        buffer.pos(bounds.minX, maxY, bounds.minZ).tex(minU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();

        switch (facing) {
            case NORTH:
                //North
                buffer.pos(bounds.minX, minY, bounds.minZ).tex(minU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.minX, maxY, bounds.minZ).tex(minU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.maxX, maxY, bounds.minZ).tex(maxU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.maxX, minY, bounds.minZ).tex(maxU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();

            case SOUTH:
                //South
                buffer.pos(bounds.maxX, minY, bounds.maxZ).tex(maxU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.maxX, maxY, bounds.maxZ).tex(maxU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.minX, maxY, bounds.maxZ).tex(minU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.minX, minY, bounds.maxZ).tex(minU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();

            case WEST:
                //West
                buffer.pos(bounds.minX, minY, bounds.maxZ).tex(minU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.minX, maxY, bounds.maxZ).tex(maxU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.minX, maxY, bounds.minZ).tex(maxU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.minX, minY, bounds.minZ).tex(minU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();

            case EAST:
                //East
                buffer.pos(bounds.maxX, minY, bounds.minZ).tex(minU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.maxX, maxY, bounds.minZ).tex(maxU, minV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.maxX, maxY, bounds.maxZ).tex(maxU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();
                buffer.pos(bounds.maxX, minY, bounds.maxZ).tex(minU, maxV).lightmap(skyLight, blockLight).color(r, g, b, a).endVertex();

        }
    }
}
