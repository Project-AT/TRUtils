package projectat.trutils.core.fluids;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import projectat.trutils.Main;
import projectat.trutils.core.aura.ExtraAuras;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class FluidAura extends Fluid {

    public static final Map<IAuraType, Fluid> fluidsMapping = new HashMap<>();

    public static Fluid auraOverworld = new FluidAura(NaturesAuraAPI.TYPE_OVERWORLD);
    public static Fluid auraNether = new FluidAura(NaturesAuraAPI.TYPE_NETHER);
    public static Fluid auraEnd = new FluidAura(NaturesAuraAPI.TYPE_END);
    public static Fluid auraUnderworld = new FluidAura(ExtraAuras.TYPE_UNDERWORLD);

    public static void registerFluids() {
        FluidRegistry.registerFluid(auraOverworld);
        FluidRegistry.registerFluid(auraEnd);
        FluidRegistry.registerFluid(auraNether);
        FluidRegistry.registerFluid(auraUnderworld);
        FluidRegistry.addBucketForFluid(auraOverworld);
        FluidRegistry.addBucketForFluid(auraEnd);
        FluidRegistry.addBucketForFluid(auraNether);
        FluidRegistry.addBucketForFluid(auraUnderworld);
    }

    private final IAuraType auraType;

    public IAuraType getAuraType() {
        return auraType;
    }

    public FluidAura(IAuraType auraType) {
        super("aura_" + auraType.getName().getPath(),
            new ResourceLocation(Main.MODID, "blocks/aura_still"),
            new ResourceLocation(Main.MODID, "blocks/aura_flow"),
            auraType.getColor());

        setGaseous(true);
        this.auraType = auraType;
        fluidsMapping.put(auraType, this);
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        return super.getLocalizedName(stack);
    }

}
