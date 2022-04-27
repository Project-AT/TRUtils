package projectat.trutils.core.aura;

import de.ellpeck.naturesaura.api.aura.type.BasicAuraType;
import projectat.trutils.Main;
import net.minecraft.util.ResourceLocation;
import sblectric.lightningcraft.init.LCDimensions;

public class ExtraAuras {

    public static final BasicAuraType TYPE_UNDERWORLD =
            new BasicAuraType(new ResourceLocation(Main.MODID, "underworld"), null, 0x07163c, 0).register();

    public static void postInit() {
        //雷电的DimensionType是在init创建的，提前call会炸
        TYPE_UNDERWORLD.addDimensionType(LCDimensions.UNDERWORLD_TYPE);
    }

}
