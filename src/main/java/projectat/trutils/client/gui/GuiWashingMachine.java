package projectat.trutils.client.gui;

import com.google.common.collect.Lists;
import projectat.trutils.core.container.ContainerWashingMachine;
import projectat.trutils.core.network.ATNetwork;
import projectat.trutils.core.network.PacketFillGuiFluidTank;
import projectat.trutils.core.tile.TileWashingMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;

import java.io.IOException;
import java.util.List;

public class GuiWashingMachine extends GuiContainer {

    public static final ResourceLocation BACKGROUND = new ResourceLocation("trutils", "textures/gui/gui_washing_machine.png");

    private final ContainerWashingMachine container;
    private final TileWashingMachine tile;

    public GuiWashingMachine(ContainerWashingMachine inventorySlotsIn) {
        super(inventorySlotsIn);
        this.tile = inventorySlotsIn.getWashingMachine();
        this.container = inventorySlotsIn;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);


        float process = getProcess();
        float verticalProcess = MathHelper.clamp((process * 34 - 7) / 27, 0, 1);

        renderVerticalBarTB(this.guiLeft + 88, this.guiTop + 29, 16, 27, 223, 65, verticalProcess);

        float horizontalProcessInput = MathHelper.clamp(process * 34 / 12, 0, 1);
        renderHorizontalBar(this.guiLeft + 46, this.guiTop + 34, 45, 4, 177, 76, horizontalProcessInput);


        float horizontalProcessOutput = MathHelper.clamp((process * 34 - 22) / 12, 0, 1);
        renderHorizontalBar(this.guiLeft + 94, this.guiTop + 40, 45, 10, 177, 80, horizontalProcessOutput);

        float energyPercent = getEnergyPercent();
        renderVerticalBarBT(this.guiLeft + 7, this.guiTop + 11, 18, 63, 177, 2, energyPercent);

        FluidStack input = tile.getInputTank().getLastFluid();
        float inputTankPercent = tile.getInputTank().getPercentage(partialTicks);
        renderFluid(input, inputTankPercent, this.guiLeft + 28, this.guiTop + 11, 16, 63);
        FluidStack output = tile.getOutputTank().getLastFluid();
        float outputTankPercent = tile.getOutputTank().getPercentage(partialTicks);
        renderFluid(output, outputTankPercent, this.guiLeft + 140, this.guiTop + 11, 16, 63);


        mc.getTextureManager().bindTexture(BACKGROUND);

        drawTexturedModalRect(this.guiLeft + 37, this.guiTop + 11, 204, 2, 7, 63);
        drawTexturedModalRect(this.guiLeft + 149, this.guiTop + 11, 204, 2, 7, 63);

    }

    public void renderVerticalBarBT(int x, int y, int w, int h, int u, int v, float percent) {
        int pxFilled = MathHelper.ceil(percent * h);
        drawTexturedModalRect(x, y + h - pxFilled, u, v + h - pxFilled, w, pxFilled);
    }

    public void renderVerticalBarTB(int x, int y, int w, int h, int u, int v, float percent) {
        int pxFilled = MathHelper.ceil(percent * h);
        drawTexturedModalRect(x, y, u, v, w, pxFilled);
    }


    public void renderHorizontalBar(int x, int y, int w, int h, int u, int v, float percent) {
        int pxFilled = MathHelper.ceil(percent * w);
        drawTexturedModalRect(x, y, u, v, pxFilled, h);
    }

    public void renderFluid(FluidStack fluidStack, float percent, int x, int y, int w, int h) {
        if (fluidStack != null && fluidStack.amount > 0) {
            int c = fluidStack.getFluid().getColor(fluidStack);
            int b = c & 255;
            int g = c >> 8 & 255;
            int r = c >> 16 & 255;

            int pxFilled = MathHelper.ceil(percent * h);
            GlStateManager.color(r, g, b, 1f);
            ResourceLocation rl = fluidStack.getFluid().getStill(fluidStack);
            TextureAtlasSprite tas = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(rl.toString());
            if (tas == null) {
                tas = mc.getTextureMapBlocks().getMissingSprite();
            }
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            drawTexturedModalRect(x, y + h - pxFilled, tas, w, pxFilled);
        }
    }


    private float getEnergyPercent() {
        return (float) container.getGuiData()[0] / TileWashingMachine.maxEnergy;
    }

    private float getProcess() {
        return (float) container.getGuiData()[1] / container.getGuiData()[2];
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        ItemStack held = this.mc.player.inventory.getItemStack();
        if (!held.isEmpty() && FluidUtil.getFluidHandler(held) != null) {
            if (isPointInRegion(28, 11, 16, 63, mouseX, mouseY)) {
                ATNetwork.getInstance().sendToServer(new PacketFillGuiFluidTank(0, mouseButton));
            } else if (isPointInRegion(140, 11, 16, 63, mouseX, mouseY)) {
                ATNetwork.getInstance().sendToServer(new PacketFillGuiFluidTank(1, mouseButton));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String containerName = I18n.format("tile.trutils.washing_machine.name");
        fontRenderer.drawString(containerName, xSize / 2 - fontRenderer.getStringWidth(containerName) / 2, 1, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 94, 0x404040);
    }

    @Override
    protected void renderHoveredToolTip(int x, int y) {
        super.renderHoveredToolTip(x, y);

        if (isPointInRegion(8, 11, 16, 63, x, y)) {
            List<String> text = Lists.newArrayList();
            text.add(container.getGuiData()[0] + " / " + TileWashingMachine.maxEnergy + " RF");
            drawHoveringText(text, x, y, fontRenderer);
        } else if (isPointInRegion(28, 11, 16, 63, x, y)) {
            List<String> text = Lists.newArrayList();
            getFluidTooltip(tile.getInputTank(), text);
            drawHoveringText(text, x, y, fontRenderer);
        } else if (isPointInRegion(140, 11, 16, 63, x, y)) {
            List<String> text = Lists.newArrayList();
            getFluidTooltip(tile.getOutputTank(), text);
            drawHoveringText(text, x, y, fontRenderer);
        }

    }


    private void getFluidTooltip(FluidTank tank, List<String> curTip) {
        Fluid fluid = null;
        int amt = 0;
        int capacity;

        if (tank.getFluid() != null) {
            fluid = tank.getFluid().getFluid();
            amt = tank.getFluidAmount();
        }
        capacity = tank.getCapacity();

        if (fluid == null || amt == 0 || capacity == 0) {
            curTip.add(amt + "/" + capacity + " mb");
            curTip.add(TextFormatting.GRAY + I18n.format("gui.liquid.empty"));
        } else {
            curTip.add(amt + "/" + capacity + " mb");
            curTip.add(TextFormatting.GRAY + fluid.getLocalizedName(new FluidStack(fluid, amt)));
        }
    }

}
