package ikexing.atutils.core.potions;

import ikexing.atutils.ATUtils;
import net.minecraft.potion.Potion;

public class PotionDrowsy extends Potion {

    public PotionDrowsy() {
        super(false, 0x6688FF);
        this.setPotionName("effect." + ATUtils.MODID + ".drowsy");
        this.setRegistryName(ATUtils.MODID + ":drowsy");
    }

    
}
