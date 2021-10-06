package ikexing.atutils;

import cn.hutool.core.util.ReflectUtil;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import ikexing.atutils.core.item.AuthorFood;
import ikexing.atutils.core.network.NetworkManager;
import ikexing.atutils.core.ritual.RitualMagneticAttraction;
import mana_craft.init.ManaCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vazkii.botania.common.block.ModBlocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(
    modid = ATUtils.MODID,
    name = ATUtils.NAME,
    version = ATUtils.VERSION,
    dependencies = ATUtils.dependencies
)
public class ATUtils {

    public static final String MODID = "atutils";
    public static final String NAME = "AutoTech Utils";
    public static final String VERSION = "1.1.5";
    public static final String dependencies = "required-after:crafttweaker;after:contenttweaker;after:twilightforest;after:botania;after:contenttweaker;before:mana_craft";

    public static RitualBase ritualMa;
    public static Block circuitry;
    public static Item magneticAttraction =
            new Item().setRegistryName("magnetic_attraction").setTranslationKey(MODID + "." + "magnetic_attraction");

    public static Logger logger;

    @EventHandler
    public void onConstruct(FMLConstructionEvent event) {
        AuthorFood.downloadAvatar();
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        NetworkManager.register();
        RitualRegistry.addRitual(ritualMa = new RitualMagneticAttraction());
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) throws Exception {
        if (Loader.isModLoaded("mana_craft")) {
            ManaCraftOrichalcum();
        }
    }

    private void ManaCraftOrichalcum() throws Exception {
        Field field = ReflectUtil.getField(ManaCraftBlocks.class, "orichalcum_block");
        ReflectUtil.setAccessible(field);
        Field modifiersField = ReflectUtil.getField(Field.class, "modifiers");
        ReflectUtil.setAccessible(modifiersField).setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, ModBlocks.storage);
    }
}
