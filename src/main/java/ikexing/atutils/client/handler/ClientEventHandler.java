package ikexing.atutils.client.handler;

import ikexing.atutils.client.render.BlockOutlineRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
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

    private static BlockOutlineRender renderer = new BlockOutlineRender();

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {

        renderer.init();


    }

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {

        // step 1: collect furnaces
        Minecraft mc = Minecraft.getMinecraft();
        World world = Objects.requireNonNull(mc.world);
        Map<BlockPos, IBlockState> furnaceCollection = new HashMap<>();
        for (TileEntity tileEntity : world.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityFurnace) {
                furnaceCollection.put(tileEntity.getPos(), world.getBlockState(tileEntity.getPos()));
            }
        }
        if (furnaceCollection.isEmpty()) return;
        renderer.setRenderList(furnaceCollection);
        renderer.updateFrameBufferSize();
        renderer.renderToBuffer(event.getPartialTicks());
        renderer.renderToScreen();
    }
}
