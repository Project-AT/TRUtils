package ikexing.atutils.core.events;

import ikexing.atutils.ATUtils;
import ikexing.atutils.core.ATHacks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.Optional;

public class EventLootTableLoad {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLootTableLoad(LootTableLoadEvent event) {

        // The following code does these things:
        // 1) replace iron_ingot with rusty_iron_ingot
        // 2) remove any non-vanilla item

        for (LootPool lootPool : ATHacks.getPools(event.getTable())) {

            // for all chests only, excluding entities or other things
            if (!event.getName().getPath().startsWith("chests")) { return; }

            Iterator<LootEntry> entriesIterator = ATHacks.getEntries(lootPool).iterator();
            while (entriesIterator.hasNext()) {

                LootEntry entry = entriesIterator.next();

                if (entry instanceof LootEntryItem) {
                    LootEntryItem entryItem = (LootEntryItem) entry;
                    Item item = entryItem.item;

                    Optional.ofNullable(item.getRegistryName())
                            .ifPresent(resourceLocation -> {
                                // check if it is a vanilla item
                                if ("minecraft".equals(resourceLocation.getNamespace())) {
                                    // and this item is "minecraft:iron_ingot"
                                    if (Items.IRON_INGOT.equals(item)) {
                                        Optional.ofNullable(Item.getByNameOrId("contenttweaker:rusty_iron_ingot"))
                                                .ifPresent(rustyIronIngot -> {
                                                    ATUtils.logger.info("Replacing \"minecraft:iron_ingot\" with \"contenttweaker:rusty_iron_ingot\" for pool({}#{})", event.getName(), lootPool.getName());
                                                    ATHacks.setItem(entryItem, rustyIronIngot);
                                                });
                                    }
                                // if it is not a vanilla item
                                } else {
                                    // remove this entry that contains non-vanilla items
                                    entriesIterator.remove();
                                    ATUtils.logger.info("Removed non-vanilla item \"{}\" from pool({}#{})", item.getRegistryName(), event.getName(), lootPool.getName());
                                }

                            });
                }

                if (entry instanceof LootEntryTable) {
                    LootEntryTable entryTable = (LootEntryTable) entry;
                    ResourceLocation table = ATHacks.getTable(entryTable);
                    // remove injected table, which is almost contains non-vanilla items
                    if (!entry.getEntryName().contains("minecraft")) {
                        ATHacks.setTable(entryTable, LootTableList.EMPTY);
                        ATUtils.logger.info("Removed table({}) from pool({}#{})", table.toString(), event.getName(), lootPool.getName());
                    }

                }

            }
        }

    }

}
