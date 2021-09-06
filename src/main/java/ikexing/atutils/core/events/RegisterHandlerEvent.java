package ikexing.atutils.core.events;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.mysticallib.event.RegisterContentEvent;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.block.BlockNoEnergyTable;
import ikexing.atutils.core.item.AuthorFood;
import ikexing.atutils.core.item.AuthorFood.AuthorInformation;
import ikexing.atutils.core.item.CrudeSteel;
import ikexing.atutils.core.item.botania.AdvanceStickThunder;
import ikexing.atutils.core.item.botania.StickThunder;
import ikexing.atutils.core.ritual.entity.EntityRitualMagneticAttraction;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@EventBusSubscriber
public class RegisterHandlerEvent {

    @SubscribeEvent
    public static void onItemRegistry(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(StickThunder.INSTANCE);
        registry.register(ATUtils.magneticAttraction);
        registry.register(AdvanceStickThunder.INSTANCE);
        registry.register(BlockNoEnergyTable.ITEM_BLOCK);
        registry.registerAll(CrudeSteel.ITEMS.toArray(new Item[0]));
        registry.registerAll(AuthorFood.AUTHOR_QQ_NUMBER.stream().map(AuthorInformation::of).peek(AuthorFood.ITEM_FOODS::add).toArray(ItemFood[]::new));
    }

    @SubscribeEvent
    public static void onBlockRegistry(Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(BlockNoEnergyTable.INSTANCE);
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        AuthorFood.convert();
        regModel(ATUtils.magneticAttraction);
        regModel(BlockNoEnergyTable.ITEM_BLOCK);
        CrudeSteel.ITEMS.forEach(RegisterHandlerEvent::regModel);
        AuthorFood.ITEM_FOODS.forEach(RegisterHandlerEvent::regModel);
    }

    @SubscribeEvent
    public static void onRegisterContent(RegisterContentEvent event) {
        LibRegistry.registerEntity(EntityRitualMagneticAttraction.class);
        LibRegistry.registerEntityRenderer(EntityRitualMagneticAttraction.class, new RenderNull.Factory());
    }

    public static void regModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }
}
