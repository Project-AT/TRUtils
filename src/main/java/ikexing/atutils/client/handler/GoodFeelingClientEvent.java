package ikexing.atutils.client.handler;

import ikexing.atutils.ATUtils;
import ikexing.atutils.core.events.GoodFeelingEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTwigWand;

@Mod.EventBusSubscriber(Side.CLIENT)
public class GoodFeelingClientEvent {

    @SubscribeEvent
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        Profiler profiler = mc.profiler;

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            RayTraceResult pos = mc.objectMouseOver;
            if (pos != null) {
                IBlockState state = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.world.getBlockState(pos.getBlockPos()) : null;
                Block block = state == null ? null : state.getBlock();
                TileEntity tile = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.world.getTileEntity(pos.getBlockPos()) : null;

                if (PlayerHelper.hasHeldItemClass(mc.player, ItemTwigWand.class) &&
                        block == ModBlocks.alfPortal && state.getValue(BotaniaStateProps.ALFPORTAL_STATE) != AlfPortalState.OFF) {
                    profiler.startSection("atutils-wand");
                    renderHUD(mc, event.getResolution(), (TileAlfPortal) tile);
                    profiler.endSection();
                }
            }
        }
    }

    public static void renderHUD(Minecraft mc, ScaledResolution res, TileAlfPortal tile) {
        String name = I18n.format("item.atutils.good_feeling_level.name");
        int color = 0x4444FF;

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
        int y = res.getScaledHeight() / 2 + 10;
        mc.fontRenderer.drawStringWithShadow(name, x, y, color);

        x = res.getScaledWidth() / 2 - 33;
        y = res.getScaledHeight() / 2 + 20;

//        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
//        for (int i = 0; i < 5; i++) {
//            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(GoodFeelingLevel.INSTANCE), x + (13 * i), y);
//        }
//        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        mc.getTextureManager().bindTexture(new ResourceLocation(ATUtils.MODID, "textures/items/good_feeling_level.png"));
//
//        x += 13;
        for (int i = 0; i < 5; i++) {
            renderItemGoodFeelingLevel(x + (13 * i), y, 1);
        }
        double experience = tile.getUpdateTag().getDouble("packetGoodFeelingExperience");
        if (experience != 0) {
            int level = GoodFeelingEvent.getLevel(experience);
            for (int i = 0; i < level; i++) {
                renderItemGoodFeelingLevel(x + (13 * i), y, 5);
            }
            double v = experience / GoodFeelingEvent.GOOD_FEELING_LEVEL.inverse().get(level + 1);
            renderItemGoodFeelingLevel(x + (13 * (level)), y, (int) Math.round(v * 5));
        } else {
            String text = I18n.format("botaniamisc.statusUnknown");
            x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
            mc.fontRenderer.drawString(text, x, y + 3, color);
        }

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
    }

    public static void renderItemGoodFeelingLevel(int x, int y, int progress) {
        int size = 16;
        int standard = (255 / 5) * progress;
        if (progress == 1) standard += 10;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x, y + size, 0).tex(0, 1).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(x + size, y + size, 0).tex(1, 1).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(x + size, y, 0).tex(1, 0).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(x, y, 0).tex(0, 0).color(standard, standard, standard, standard).endVertex();
        tessellator.draw();
    }

}
