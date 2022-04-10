package ikexing.atutils.core;

import ikexing.atutils.core.aura.ExtraAuras;
import ikexing.atutils.core.fluids.FluidAura;
import ikexing.atutils.core.network.ATNetwork;
import ikexing.atutils.core.tile.TileWashingMachine;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ikexing.atutils.ATUtils.MODID;

public class CommonProxy {
    public void preInit() {
        FluidAura.registerFluids();
        ATNetwork.init();
    }

    public void init() {

        GameRegistry.registerTileEntity(TileWashingMachine.class, new ResourceLocation(MODID, "washing_machine"));
    }

    public void postInit() {
        ExtraAuras.postInit();
    }
}
