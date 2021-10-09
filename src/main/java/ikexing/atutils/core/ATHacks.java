package ikexing.atutils.core;

import ikexing.atutils.ATUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ATHacks {

    private ATHacks() {}

    public static List<LootPool> getPools(LootTable table) {
        try {
            Field field = getField(LootTable.class, "field_186466_c", "pools");
            return (List<LootPool>) field.get(table);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ATUtils.logger.error("Could not load LootTable #pools", e);
        }

        return new ArrayList<>(0);
    }

    public static List<LootEntry> getEntries(LootPool pool) {
        try {
            Field field = getField(LootPool.class, "field_186453_a", "lootEntries");
            return (List<LootEntry>) field.get(pool);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ATUtils.logger.error("Could not load LootPool #lootEntries", e);
        }

        return new ArrayList<>(0);
    }

    public static ResourceLocation getTable(LootEntryTable entryTable) {
        try {
            Field field = getField(LootEntryTable.class, "field_186371_a", "table");
            return (ResourceLocation) field.get(entryTable);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ATUtils.logger.error("Could not load LootEntryTable #table", e);
        }

        return new ResourceLocation("", "");
    }

    public static void setTable(LootEntryTable entryTable, ResourceLocation tableLocation) {
        try {
            Field field = getField(LootEntryTable.class, "field_186371_a", "table");
            field.set(entryTable, tableLocation);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ATUtils.logger.error("Could not modify LootEntryTable #table", e);
        }
    }

    public static void setItem(LootEntryItem entry, Item item) {
        try {
            Field field = getField(LootEntryItem.class, "field_186368_a", "item");
            field.set(entry, item);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ATUtils.logger.error("Could not modify LootEntryItem #item", e);
        }
    }

    public static Field getField(Class<?> cls, String srgName, String mcpName) throws NoSuchFieldException {
        return getOptionalField(cls, srgName, mcpName)
                .orElseThrow(() -> new NoSuchFieldException(cls.getName() + " " + srgName + " # " + mcpName));
    }

    public static Optional<Field> getOptionalField(Class<?> cls, String srgName, String mcpName) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(field -> field.getName().equals(srgName) || field.getName().equals(mcpName))
                .peek(field -> field.setAccessible(true))
                .findFirst();
    }

}
