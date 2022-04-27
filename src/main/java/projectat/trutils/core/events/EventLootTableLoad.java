package projectat.trutils.core.events;

import projectat.trutils.Main;
import projectat.trutils.core.ATHacks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventLootTableLoad {

    static List<ResourceLocation> vanillaTables = Stream.of(
            "abandoned_mineshaft",
            "desert_pyramid",
            "end_city_treasure",
            "igloo_chest",
            "jungle_temple",
            "jungle_temple_dispenser",
            "nether_bridge",
            "simple_dungeon",
            "spawn_bonus_chest",
            "stronghold_corridor",
            "stronghold_crossing",
            "stronghold_library",
            "village_blacksmith",
            "woodland_mansion"
    ).map(s -> new ResourceLocation("minecraft", "chests/" + s)).collect(Collectors.toList());

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLootTableLoad(LootTableLoadEvent event) {

        // The following code does these things:
        // 1) replace iron_ingot with rusty_iron_ingot
        // 2) remove any non-vanilla item
        // 3) inject some custom pool

        for (LootPool lootPool : ATHacks.getPools(event.getTable())) {

            // for all chests only, excluding entities or other things
            if (!event.getName().getPath().startsWith("chests")) { return; }
            // do not change the modification of trutils
            if (event.getName().getNamespace().startsWith("trutils")) { return; }

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
                                                    Main.logger.info("Replacing \"minecraft:iron_ingot\" with \"contenttweaker:rusty_iron_ingot\" for pool({}#{})", event.getName(), lootPool.getName());
                                                    ATHacks.setItem(entryItem, rustyIronIngot);
                                                });
                                    }
                                // if it is not a vanilla item
                                } else {
                                    // remove this entry that contains non-vanilla items
                                    entriesIterator.remove();
                                    Main.logger.info("Removed non-vanilla item \"{}\" from pool({}#{})", item.getRegistryName(), event.getName(), lootPool.getName());
                                }

                            });
                }

                if (entry instanceof LootEntryTable) {
                    LootEntryTable entryTable = (LootEntryTable) entry;
                    ResourceLocation table = ATHacks.getTable(entryTable);
                    // remove injected table, which is almost contains non-vanilla items
                    if (!entry.getEntryName().contains("minecraft")) {
                        ATHacks.setTable(entryTable, LootTableList.EMPTY);
                        Main.logger.info("Removed table({}) from pool({}#{})", table.toString(), event.getName(), lootPool.getName());
                    }

                }

            }
        }

        // inject "trutils:chests/inject/books" to "mysticalworld:chests/hut"
        if (event.getName().equals(new ResourceLocation("mysticalworld","chests/hut"))) {
            RandomValueRange range = new RandomValueRange(1, 1);
            LootPool pool = new LootPool(new LootEntry[]{new LootEntryTable(new ResourceLocation("trutils", "chests/inject/books"), 1, 0, new LootCondition[0], "books")}, new LootCondition[0], range, range, "trutils_inject");
            event.getTable().addPool(pool);
        }

        // inject "trutils:chests/inject/potions" to the table of the list
        if (vanillaTables.contains(event.getName())) {
            RandomValueRange range = new RandomValueRange(1, 1);
            LootPool pool = new LootPool(new LootEntry[]{new LootEntryTable(new ResourceLocation("trutils", "chests/inject/potions"), 1, 0, new LootCondition[0], "potions")}, new LootCondition[0], range, range, "trutils_inject");
            event.getTable().addPool(pool);
        }

    }

}
