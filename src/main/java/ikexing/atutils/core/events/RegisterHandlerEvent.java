package ikexing.atutils.core.events;

import ikexing.atutils.core.item.AuthorFood;
import ikexing.atutils.core.item.AuthorFood.AuthorInformation;
import ikexing.atutils.core.item.CrudeSteel;
import ikexing.atutils.core.item.botania.AdvanceStickThunder;
import ikexing.atutils.core.item.botania.StickThunder;
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
        registry.register(AdvanceStickThunder.INSTANCE);
        registry.registerAll(CrudeSteel.ITEMS.toArray(new Item[0]));
        registry.registerAll(AuthorFood.AUTHOR_QQ_NUMBER.stream().map(AuthorInformation::of).peek(AuthorFood.ITEM_FOODS::add).toArray(ItemFood[]::new));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        AuthorFood.convert();
        CrudeSteel.ITEMS.forEach(RegisterHandlerEvent::regModel);
        AuthorFood.ITEM_FOODS.forEach(RegisterHandlerEvent::regModel);
    }

    public static void regModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }
}
