package projectat.trutils.core.container.gui;

import projectat.trutils.Main;
import projectat.trutils.core.container.ContainerBunker;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiBunker extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(Main.MODID, "textures/gui/fuel_hatch_gui.png");

    private final ContainerBunker container;

    public GuiBunker(ContainerBunker container) {
        super(container);
        this.container = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int left = (this.width - this.xSize) / 2;
        int top = (this.height - this.ySize) / 2;
        if (container.isWork() != 0) {
            this.drawTexturedModalRect(left + 67, top + 34, 176, 0, container.bunker.getWorld().rand.nextBoolean() ? 23 : 0, 16);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String text = I18n.format("tile.modularmachinery.blockbunker.default.name");
        this.fontRenderer.drawString(text, this.xSize / 2 - this.fontRenderer.getStringWidth(text) / 2, 6, 4210752);
    }


}
