package ikexing.atutils.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.Botania;

import java.util.Random;

public class NetworkManager {

    public static void register() {
        MagneticAttraction.CHANNEL.register(MagneticAttraction.class);
    }

    public static class MagneticAttraction {
        private static final String NAME = "MAGNETIC_ATTRACTION";
        private static final FMLEventChannel CHANNEL = NetworkRegistry.INSTANCE.newEventDrivenChannel(NAME);

        public static void sendClientCustomPacket(BlockPos pos, String oreName, int dim) {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeBlockPos(pos);
            buffer.writeString(oreName);

            CHANNEL.sendToDimension(new FMLProxyPacket(buffer, NAME), dim);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onClientCustomPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
            ByteBuf buf = event.getPacket().payload();
            PacketBuffer buffer = new PacketBuffer(buf);
            BlockPos pos = buffer.readBlockPos();
            String oreName = buffer.readString(32);

            Minecraft mc = Minecraft.getMinecraft();

            mc.addScheduledTask(() -> {
                WorldClient world = mc.world;

                Botania.proxy.setWispFXDepthTest(false);
                Random rand = new Random(oreName.hashCode() ^ world.rand.nextLong());
                Botania.proxy.wispFX(pos.getX() + world.rand.nextFloat(), pos.getY() + world.rand.nextFloat(), pos.getZ() + world.rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.45F, 0F, 10);
                Botania.proxy.setWispFXDepthTest(true);
            });
        }
    }

}
