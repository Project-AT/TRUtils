package ikexing.atutils;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import epicsquid.roots.integration.crafttweaker.Herbs;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import ikexing.atutils.core.advancement.VisitVillageTrigger;
import ikexing.atutils.core.container.gui.GuiProxy;
import ikexing.atutils.core.events.EventLootTableLoad;
import ikexing.atutils.core.item.AuthorFood;
import ikexing.atutils.core.network.NetworkManager;
import ikexing.atutils.core.ritual.RitualMagneticAttraction;
import mana_craft.init.ManaCraftBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import vazkii.botania.common.block.ModBlocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

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
    public static final String dependencies = "required-after:crafttweaker;after:contenttweaker;required-after:mixinbooter;after:twilightforest;after:botania;before:mana_craft";

    public static final List<String> CANCEL_ORES = Lists.newArrayList("ingot", "Glass", "nugget", "dust", "coal", "gem", "stone", "ore");

    public static RitualBase ritualMa;
    public static Block circuitry;
    public static Block bunker;
    public static Item equivalentFuel = new Item().setRegistryName("equivalent_fuel").setTranslationKey(MODID + ".equivalent_fuel");
    public static Item magneticAttraction = new Item().setRegistryName("magnetic_attraction").setTranslationKey(MODID + ".magnetic_attraction");

    public static Logger logger;

    @Mod.Instance
    public static ATUtils instance;

    @EventHandler
    public void onConstruct(FMLConstructionEvent event) {
        AuthorFood.downloadAvatar();
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        NetworkManager.register();
        RitualRegistry.addRitual(ritualMa = new RitualMagneticAttraction());
        CriteriaTriggers.register(VisitVillageTrigger.INSTANCE);
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) throws Exception {
        if (Loader.isModLoaded("mana_craft")) {
            ManaCraftOrichalcum();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
        modifyRootSpells();
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(EventLootTableLoad.class);
    }

    private void ManaCraftOrichalcum() throws Exception {
        Field field = ReflectUtil.getField(ManaCraftBlocks.class, "orichalcum_block");
        ReflectUtil.setAccessible(field);
        Field modifiersField = ReflectUtil.getField(Field.class, "modifiers");
        ReflectUtil.setAccessible(modifiersField).setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, ModBlocks.storage);
    }

    public static boolean isCancel(ItemStack stack) {
        return Arrays.stream(OreDictionary.getOreNames())
                .filter(it -> CANCEL_ORES.stream().anyMatch(it::contains))
                .flatMap(it -> OreDictionary.getOres(it).stream())
                .anyMatch(stack::isItemEqual);
    }

    private void modifyRootSpells() {
        SpellBase chrysopoeiaSpell = SpellRegistry.getSpell("spell_chrysopoeia");
        PropertyTable chrysopoeiaSpellPropertiesrops = chrysopoeiaSpell.getProperties();
        Property<SpellBase.SpellCost> infernalBulbProp = chrysopoeiaSpellPropertiesrops.get("cost_" + Herbs.infernal_bulb.getHerbName());
        chrysopoeiaSpellPropertiesrops.getProperties().remove(infernalBulbProp);
        Property<SpellBase.SpellCost> spiritHerbProp = chrysopoeiaSpellPropertiesrops.get("cost_" + Herbs.spirit_herb.getHerbName());
        SpellBase.SpellCost spiritHerbNewCost = new SpellBase.SpellCost(Herbs.spirit_herb.getHerbName(), 0.5);
        chrysopoeiaSpellPropertiesrops.set(spiritHerbProp, spiritHerbNewCost);
    }

}
