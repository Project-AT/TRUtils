package projectat.trutils.client;

import projectat.trutils.client.tesr.TileWashingMachineRender;
import projectat.trutils.core.CommonProxy;
import projectat.trutils.core.tile.TileWashingMachine;
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
