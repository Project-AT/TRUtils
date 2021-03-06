package projectat.trutils;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import crafttweaker.api.item.IIngredient;
import epicsquid.roots.integration.crafttweaker.Herbs;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import projectat.trutils.core.CommonProxy;
import projectat.trutils.core.container.gui.GuiProxy;
import projectat.trutils.core.events.EventLootTableLoad;
import projectat.trutils.core.item.AuthorFood;
import projectat.trutils.core.ritual.RitualMagneticAttraction;
import mana_craft.init.ManaCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import sblectric.lightningcraft.api.util.JointList;
import sblectric.lightningcraft.init.LCItems;
import sblectric.lightningcraft.recipes.LightningTransformRecipes;
import teamroots.embers.RegistryManager;
import teamroots.embers.recipe.BoreOutput;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.WeightedItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod(
        modid = Main.MODID,
        name = Main.NAME,
        version = Main.VERSION,
        dependencies = Main.dependencies
)
public class Main {

    public static final String MODID = "trutils";
    public static final String NAME = "ThirdRebirth Utils";
    public static final String VERSION = "1.1.5";
    public static final String dependencies = "required-after:crafttweaker;after:contenttweaker;required-after:mixinbooter;after:twilightforest;after:botania;before:mana_craft";

    public static final Map<IIngredient, Double> ALF_PORTAL_EXPERIENCE = Maps.newHashMap();
    public static final Map<String, RecipeElvenTrade> RECIPE_ELVEN_TRADES = Maps.newHashMap();

    public static final List<String> CANCEL_ORES = Lists.newArrayList(
            "ingot", "Glass", "nugget", "dust", "coal", "gem", "stone", "ore", "shard", "clamp"
    );
    public static final List<Pair<String, Integer>> CANCEL_ITEMS = Lists.newArrayList(
            Pair.of("minecraft:ender_pearl", 0),
            Pair.of("enderio:item_material", 4),
            Pair.of("embers:plate_caminite", 0),
            Pair.of("mysticalworld:mud_block", 0),
            Pair.of("thermalfoundation:rockwool", 7),
            Pair.of("pneumaticcraft:empty_pcb", 100),
            Pair.of("extrautils2:decorativeglass", 0),
            Pair.of("appliedenergistics2:material", 5),
            Pair.of("contenttweaker:refractory_brick", 0),
            Pair.of("appliedenergistics2:smooth_sky_stone_block", 0)
    );

    public static RitualBase ritualMa;
    public static Block circuitry;
    public static Block bunker;
    public static Item equivalentFuel = new Item().setRegistryName("equivalent_fuel").setTranslationKey(MODID + ".equivalent_fuel");
    public static Item magneticAttraction = new Item().setRegistryName("magnetic_attraction").setTranslationKey(MODID + ".magnetic_attraction");

    public static Logger logger;

    @SidedProxy(clientSide = "projectat.trutils.client.ClientProxy", serverSide = "projectat.trutils.core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Main instance;

    public static boolean isCancel(ItemStack stack) {
        return CANCEL_ITEMS.stream().anyMatch(pair -> equalStackWithPair(pair, stack)) ||
                Arrays.stream(OreDictionary.getOreNames())
                        .filter(it -> CANCEL_ORES.stream().anyMatch(it::contains))
                        .flatMap(it -> OreDictionary.getOres(it).stream())
                        .anyMatch(stack::isItemEqual);
    }

    private static boolean equalStackWithPair(Pair<String, Integer> pair, ItemStack stack) {
        return pair.getKey().equals(Objects.requireNonNull(stack.getItem().getRegistryName()).toString()) && pair.getValue() == stack.getMetadata();
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        if (Loader.isModLoaded("mana_craft")) {
            manaCraftOrichalcum();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
        modifyRootSpells();
        BotaniaAPI.elvenTradeRecipes.clear();
        BotaniaAPI.elvenTradeRecipes.addAll(RECIPE_ELVEN_TRADES.values());
        proxy.init();
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        AuthorFood.downloadAvatar();
        modifyLightningCraftDefaultRecipes();
        BotaniaAPI.oreWeights.clear();
        BotaniaAPI.oreWeightsNether.clear();
        RitualRegistry.addRitual(ritualMa = new RitualMagneticAttraction());

        proxy.preInit();
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        unregisterDemonicIngotHandlerEvent();
        setDefaultBoreOutput();
        MinecraftForge.EVENT_BUS.register(EventLootTableLoad.class);

        proxy.postInit();
    }

    private void unregisterDemonicIngotHandlerEvent() {
        ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = getListeners();
        for (Map.Entry<Object, ArrayList<IEventListener>> entry : listeners.entrySet()) {
            if (entry.getKey().getClass().getSimpleName().contains("DemonicIngotHandler")) {
                MinecraftForge.EVENT_BUS.unregister(entry.getKey());
                break;
            }
        }
    }

    private void manaCraftOrichalcum() {
        Field field = ReflectUtil.getField(ManaCraftBlocks.class, "orichalcum_block");
        ReflectUtil.setAccessible(field);
        Field modifiersField = ReflectUtil.getField(Field.class, "modifiers");
        try {
            ReflectUtil.setAccessible(modifiersField).setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, ModBlocks.storage);
        } catch (IllegalAccessException ignored) {}
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

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<Object, ArrayList<IEventListener>> getListeners() {
        ConcurrentHashMap<Object, ArrayList<IEventListener>> toReturn = null;
        try {
            EventBus eventBus = MinecraftForge.EVENT_BUS;
            Field field = eventBus.getClass().getDeclaredField("listeners");
            field.setAccessible(true);
            toReturn = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) field.get(eventBus);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private void setDefaultBoreOutput() {
        ArrayList<WeightedItemStack> weightedItemStacks = Lists.newArrayList(
                new WeightedItemStack(new ItemStack(RegistryManager.crystal_ember), 20),
                new WeightedItemStack(new ItemStack(RegistryManager.shard_ember), 60),
                new WeightedItemStack(new ItemStack(RegistryManager.dust_ember), 20)
        );

        Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation("enderio:item_material")))
                .ifPresent(item -> weightedItemStacks.add(new WeightedItemStack(new ItemStack(item, 1, 20), 50)));
        BoreOutput defaultOutput = new BoreOutput(Sets.newHashSet(), Sets.newHashSet(), weightedItemStacks);
        RecipeRegistry.setDefaultBoreOutput(defaultOutput);
    }

    private void modifyLightningCraftDefaultRecipes() {
        LightningTransformRecipes.instance().getRecipeList().clear();
        LightningTransformRecipes.instance().addRecipe(new ItemStack(LCItems.guide), new JointList<ItemStack>().join(new ItemStack(Items.BOOK)));
    }

}
