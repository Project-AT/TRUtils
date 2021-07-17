package ikexing.atutils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import mana_craft.init.ManaCraftBlocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vazkii.botania.common.block.ModBlocks;

@Mod(modid = ATUtils.MODID, name = ATUtils.NAME, version = ATUtils.VERSION, dependencies = ATUtils.dependencies)
public class ATUtils {

    public static final String MODID = "atutils";
    public static final String NAME = "AutoTech Utils";
    public static final String VERSION = "1.1.5";
    public static final String dependencies = "required-after:crafttweaker;after:twilightforest;after:botania;before:mana_craft";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ManaCraftOrichalcum();
    }

    public void ManaCraftOrichalcum() {
        try {
            Field field = ManaCraftBlocks.class.getDeclaredField("orichalcum_block");

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, ModBlocks.storage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
