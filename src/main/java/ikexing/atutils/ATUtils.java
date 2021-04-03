package ikexing.atutils;

import com.google.common.collect.BiMap;
import ikexing.atutils.botania.subtitle.SubTileHydroangeasModified;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.lib.LibBlockNames;

import java.lang.reflect.Field;

import static ikexing.atutils.core.config.ATConfig.HydroangeasModified;

@Mod(modid = ATUtils.MODID, name = ATUtils.NAME, version = ATUtils.VERSION, dependencies = ATUtils.dependencies)
public class ATUtils {
    public static final String MODID = "atutils";
    public static final String NAME = "AutoTech Utils";
    public static final String VERSION = "1.0.1";
    public static final String dependencies = "required-after:crafttweaker;after:twilightforest";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        registryOverride();
    }

    @SuppressWarnings("unchecked")
    public void registryOverride() {
        final BiMap<String, Class<? extends SubTileEntity>> subTiles;
        try {
            Field field = BotaniaAPI.class.getDeclaredField("subTiles");
            field.setAccessible(true);
            subTiles = (BiMap<String, Class<? extends SubTileEntity>>) field.get(null);

            if (subTiles != null) {
                if (!HydroangeasModified)
                    subTiles.forcePut(LibBlockNames.SUBTILE_HYDROANGEAS, SubTileHydroangeasModified.class);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
