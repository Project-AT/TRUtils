package ikexing.atutils.core.events;

import cn.hutool.core.util.ReflectUtil;
import ikexing.atutils.ATUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// REFLECTION HELL

@Mod.EventBusSubscriber(modid = "atutils")
public class EventLootTableLoad {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLootTableLoad(LootTableLoadEvent event) {
        LootTableManager manager = event.getLootTableManager();
        LootTable table = event.getTable();
        List<LootPool> pools = new ArrayList<>();

        // Get pools through reflection.
        if (table.isFrozen()) {
            ATUtils.logger.debug("Skipped modifying loot table {} because it is frozen", event.getName());
        } else {
            Field field = ReflectUtil.getField(table.getClass(), "pools"); // try MCP name
            if (field == null)
                field = ReflectUtil.getField(table.getClass(), "field_186466_c"); // if null, try SRG name
            if (field == null) {
                ATUtils.logger.debug("Reflection error: {}", event.getName()); // Oops
                return;
            }
            field.setAccessible(true);
            try {
                pools = (List<LootPool>) field.get(table);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        for (LootPool pool : pools) {

            // Well, here goes.
            ResourceLocation res = event.getName();
            if (!res.getPath().startsWith("chests")) return; // only for all chests

            List<LootEntry> lootEntries = new ArrayList<>();

            // Get lootEntries through reflection.
            if (pool.isFrozen()) {
                ATUtils.logger.debug("Skipped modifying loot pool {} because it is frozen", pool.getName());
            } else {
                Field field = ReflectUtil.getField(pool.getClass(), "lootEntries"); // try MCP name
                if (field == null)
                    field = ReflectUtil.getField(table.getClass(), "field_186453_a"); // if null, try SRG name
                if (field == null) {
                    ATUtils.logger.debug("Reflection error: {}", pool.getName()); // Oops
                    return;
                }
                field.setAccessible(true);
                try {
                    lootEntries = (List<LootEntry>) field.get(pool);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            Iterator<LootEntry> it = lootEntries.iterator();

            while (it.hasNext()) {
                LootEntry entry = it.next();

                // Remove non-vanilla items.
                if (entry instanceof LootEntryItem) {
                    LootEntryItem entryItem = (LootEntryItem) entry;
                    Item item = entryItem.item;


                    if (item.getRegistryName().getNamespace() != "minecraft") {
                        it.remove();
                        ATUtils.logger.info("Removed entry item({}) from pool({}/{})", item.getRegistryName(), event.getName(), pool.getName());
                    }

                }

                // Remove non-vanilla items.
                if (entry instanceof LootEntryTable) {
                    LootEntryTable entryTable = (LootEntryTable) entry;
                    if (entryTable.table.getPath().contains("inject")) {
                        it.remove();
                        ATUtils.logger.info("Removed entry table({}) from pool({}/{})", entryTable.table.toString(), event.getName(), pool.getName());
                    }

                }

            }
        }


    }

}
