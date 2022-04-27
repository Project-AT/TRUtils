package projectat.trutils.core.events;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.mysticallib.event.RegisterContentEvent;
import projectat.trutils.Main;
import projectat.trutils.core.block.BlockEvilStone;
import projectat.trutils.core.block.BlockRustyIron;
import projectat.trutils.core.block.BlockWashingMachine;
import projectat.trutils.core.item.AuthorFood;
import projectat.trutils.core.item.AuthorFood.AuthorInformation;
import projectat.trutils.core.item.CrudeSteel;
import projectat.trutils.core.item.FlintHoe;
import projectat.trutils.core.item.botania.AdvanceStickThunder;
import projectat.trutils.core.item.botania.GoodFeelingLevel;
import projectat.trutils.core.item.botania.StickThunder;
import projectat.trutils.core.potions.PotionDrowsy;
import projectat.trutils.core.ritual.entity.EntityRitualMagneticAttraction;
import projectat.trutils.core.utils.CustomDataSerializers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class RegisterEvent {

    @SubscribeEvent
    public static void onItemRegistry(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(FlintHoe.INSTANCE);
        registry.register(StickThunder.INSTANCE);
        registry.register(Main.equivalentFuel);
        registry.register(GoodFeelingLevel.INSTANCE);
        registry.register(BlockRustyIron.ITEM_BLOCK);
        registry.register(BlockEvilStone.ITEM_BLOCK);
        registry.register(Main.magneticAttraction);
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
        registry.register(PotionDrowsy.INSTANCE);
    }

    @SubscribeEvent
    public static void onPotionTypeRegistry(RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();
        registry.register(PotionDrowsy.POTION_TYPE_DROWSY);
        registry.register(PotionDrowsy.POTION_TYPE_LONG_DROWSY);
        registry.register(PotionDrowsy.POTION_STRONG_TYPE_DROWSY);
    }

    @SubscribeEvent
    public static void onRegisterContent(RegisterContentEvent event) {
        LibRegistry.registerEntity(EntityRitualMagneticAttraction.class);
        LibRegistry.registerEntityRenderer(EntityRitualMagneticAttraction.class, new RenderNull.Factory());
    }

    @SubscribeEvent
    public static void onDataSerializerRegistry(Register<DataSerializerEntry> event) {
        event.getRegistry().register(new DataSerializerEntry(CustomDataSerializers.SERIALIZER_BLOCK_POS_SET).setRegistryName(Main.MODID, "blockpos_set"));
    }


}
