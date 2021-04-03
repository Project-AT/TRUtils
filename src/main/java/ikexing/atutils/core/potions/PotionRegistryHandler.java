package ikexing.atutils.core.potions;

import ikexing.atutils.ATUtils;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class PotionRegistryHandler {
    public static final Potion POTION_DROWSY = new PotionDrowsy();
    public static final PotionType POTION_TYPE_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy",
            new PotionEffect(POTION_DROWSY, 3600))
            .setRegistryName("drowsy");
    public static final PotionType POTION_TYPE_LONG_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy",
                    new PotionEffect(POTION_DROWSY, 9600))
                    .setRegistryName("long_drowsy");
    public static final PotionType POTION_STRONG_TYPE_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy",
                    new PotionEffect(POTION_DROWSY, 3600))
                    .setRegistryName("strong_drowsy");

    @SubscribeEvent
    public static void onPotionRegistry(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        registry.register(POTION_DROWSY);
    }

    @SubscribeEvent
    public static void onPotionTypeRegistry(RegistryEvent.Register<PotionType> event){
        IForgeRegistry<PotionType> registry = event.getRegistry();
        registry.register(POTION_TYPE_DROWSY);
        registry.register(POTION_TYPE_LONG_DROWSY);
        registry.register(POTION_STRONG_TYPE_DROWSY);
    }
}
