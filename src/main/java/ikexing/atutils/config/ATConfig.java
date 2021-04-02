package ikexing.atutils.config;

import ikexing.atutils.ATUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = ATUtils.MODID)
public class ATConfig {
    @Comment("If true, the hydroangeas will not be modified. [default: false]")
    public static boolean HydroangeasModified = false;

    @Comment("If true, the hydroangeas won't decay. [Valid only for modified results | default: true]")
    public static boolean HydroangeasDecay = true;

    @Comment("How much mana is max of hydroangeas?")
    public static int HydroangeasMaxMana = 6000;
}
