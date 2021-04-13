package ikexing.atutils.core.config;

import ikexing.atutils.ATUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = ATUtils.MODID)
public class ATConfig {
    @Comment("If true, the hydroangeas will not be modified. [default: false]")
    public static boolean HydroangeasModified = false;

    @Comment("If true, the hydroangeas won't decay. [Valid only for modified results | default: true]")
    public static boolean HydroangeasDecay = true;

    @Comment("How much mana is max of hydroangeas? [Valid only for modified results | default: 18000]")
    public static int HydroangeasMaxMana = 18000;

    @Comment("If true, the orechid will not be modified. [default: false]")
    public static boolean OrechidModified = false;

    @Comment("What's the mana consumed by the orechid each time? [Valid only for modified results | default: 6000]")
    public static int OrechidMana = 6000;

    @Comment("What's the delay use by the orechid each tick? [Valid only for modified results | default: 20]")
    public static int OrechidDelay = 20;
}
