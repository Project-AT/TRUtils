package ikexing.atutils.core.events;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.mysticallib.event.RegisterContentEvent;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.block.BlockEvilStone;
import ikexing.atutils.core.block.BlockRustyIron;
import ikexing.atutils.core.block.BlockWashingMachine;
import ikexing.atutils.core.item.AuthorFood;
import ikexing.atutils.core.item.AuthorFood.AuthorInformation;
import ikexing.atutils.core.item.CrudeSteel;
import ikexing.atutils.core.item.FlintHoe;
import ikexing.atutils.core.item.botania.AdvanceStickThunder;
import ikexing.atutils.core.item.botania.GoodFeelingLevel;
import ikexing.atutils.core.item.botania.StickThunder;
import ikexing.atutils.core.potions.PotionDrowsy;
import ikexing.atutils.core.ritual.entity.EntityRitualMagneticAttraction;
import ikexing.atutils.core.utils.CustomDataSerializers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class RegisterEvent {

    public static final Potion POTION_DROWSY = new PotionDrowsy();
    public static final PotionType POTION_TYPE_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy", new PotionEffect(POTION_DROWSY, 3600)).setRegistryName("drowsy");
    public static final PotionType POTION_TYPE_LONG_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy", new PotionEffect(POTION_DROWSY, 9600)).setRegistryName("long_drowsy");
    public static final PotionType POTION_STRONG_TYPE_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy", new PotionEffect(POTION_DROWSY, 3600)).setRegistryName("strong_drowsy");

    @SubscribeEvent
    public static void onItemRegistry(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(FlintHoe.INSTANCE);
        registry.register(StickThunder.INSTANCE);
        registry.register(ATUtils.equivalentFuel);
        registry.register(GoodFeelingLevel.INSTANCE);
        registry.register(BlockRustyIron.ITEM_BLOCK);
        registry.register(BlockEvilStone.ITEM_BLOCK);
        registry.register(ATUtils.magneticAttraction);
        registry.register(AdvanceStickThunder.INSTANCE);
        registry.register(BlockWashingMachine.ITEM_BLOCK);
        registry.registerAll(CrudeSteel.ITEMS.toArray(new Item[0]));
        registry.registerAll(AuthorFood.AUTHOR_QQ_NUMBER.stream().map(AuthorInformation::of).peek(AuthorFood.ITEM_FOODS::add).toArray(ItemFood[]::new));
    }

    @SubscribeEvent
    public static void onBlockRegistry(Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(BlockEvilStone.INSTANCE);
        registry.register(BlockRustyIron.INSTANCE);
        registry.register(BlockWashingMachine.INSTANCE);
    }

    @SubscribeEvent
    public static void onPotionRegistry(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        registry.register(POTION_DROWSY);
    }

    @SubscribeEvent
    public static void onPotionTypeRegistry(RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();
        registry.register(POTION_TYPE_DROWSY);
        registry.register(POTION_TYPE_LONG_DROWSY);
        registry.register(POTION_STRONG_TYPE_DROWSY);
    }

    @SubscribeEvent
    public static void onRegisterContent(RegisterContentEvent event) {
        LibRegistry.registerEntity(EntityRitualMagneticAttraction.class);
        LibRegistry.registerEntityRenderer(EntityRitualMagneticAttraction.class, new RenderNull.Factory());
    }

    @SubscribeEvent
    public static void onDataSerializerRegistry(Register<DataSerializerEntry> event) {
        event.getRegistry().register(new DataSerializerEntry(CustomDataSerializers.SERIALIZER_BLOCK_POS_SET).setRegistryName(ATUtils.MODID, "blockpos_set"));
    }


}
