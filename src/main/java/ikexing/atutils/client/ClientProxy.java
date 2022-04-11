package ikexing.atutils.client;

import ikexing.atutils.client.tesr.TileWashingMachineRender;
import ikexing.atutils.core.CommonProxy;
import ikexing.atutils.core.tile.TileWashingMachine;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileWashingMachine.class, new TileWashingMachineRender());
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

}
