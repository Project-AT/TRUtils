package ikexing.atutils;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.BiMap;
import ikexing.atutils.botania.module.SubTileOrechidManager;
import ikexing.atutils.botania.subtile.SubTileOrechidModifyed;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import mana_craft.init.ManaCraftBlocks;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;

@SuppressWarnings("unchecked")
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
    public void init(FMLInitializationEvent event) throws Exception {
        ManaCraftOrichalcum();
    }

    private void ManaCraftOrichalcum() throws Exception {
        Field field = ReflectUtil.getField(ManaCraftBlocks.class, "orichalcum_block");
        ReflectUtil.setAccessible(field);
        Field modifiersField = ReflectUtil.getField(Field.class, "modifiers");
        ReflectUtil.setAccessible(modifiersField).setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, ModBlocks.storage);
    }
}
