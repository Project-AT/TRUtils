package ikexing.atutils;

import com.google.common.collect.BiMap;
import ikexing.atutils.botania.subtitle.SubTileHydroangeasModified;
import ikexing.atutils.botania.subtitle.SubTileOrechidModified;
import mana_craft.init.ManaCraftBlocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static ikexing.atutils.core.config.ATConfig.*;

@Mod(modid = ATUtils.MODID, name = ATUtils.NAME, version = ATUtils.VERSION, dependencies = ATUtils.dependencies)
public class ATUtils {
    public static final String MODID = "atutils";
    public static final String NAME = "AutoTech Utils";
    public static final String VERSION = "1.1.3";
    public static final String dependencies = "required-after:crafttweaker;after:twilightforest;after:botania;before:mana_craft";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        registryOverride();
        ManaCraftOrichalcum();
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
                if (!OrechidModified)
                    subTiles.forcePut(LibBlockNames.SUBTILE_ORECHID, SubTileOrechidModified.class);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void ManaCraftOrichalcum(){
        try {
            Field field = ManaCraftBlocks.class.getDeclaredField("orichalcum_block");

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() &~ Modifier.FINAL);

            field.set(null, ModBlocks.storage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
