package ikexing.atutils.core.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import static ikexing.atutils.ATUtils.MODID;

public class ATNetwork {
    private static SimpleNetworkWrapper INSTANCE;

    public static void init() {
        INSTANCE = new SimpleNetworkWrapper(MODID);
        registerMessages();
    }

    public static SimpleNetworkWrapper getInstance() {
        return INSTANCE;
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketFillGuiFluidTank.class, PacketFillGuiFluidTank.class, 0, Side.SERVER);
    }
}
