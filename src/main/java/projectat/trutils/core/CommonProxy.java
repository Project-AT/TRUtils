package projectat.trutils.core;

import projectat.trutils.core.aura.ExtraAuras;
import projectat.trutils.core.fluids.FluidAura;
import projectat.trutils.core.network.ATNetwork;
import projectat.trutils.core.tile.TileWashingMachine;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static projectat.trutils.Main.MODID;

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
