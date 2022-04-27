package projectat.trutils.core;

import projectat.trutils.Main;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;

public final class ATHacks {

    private ATHacks() {}

    public static List<LootPool> getPools(LootTable table) {
        try {
            return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c"); // pools
        } catch (Exception e) {
            Main.logger.error("Could not load LootTable #pools", e);
        }

        return new ArrayList<>(0);
    }

    public static List<LootEntry> getEntries(LootPool pool) {
        try {
            return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a"); // lootEntries
        } catch (Exception e) {
            Main.logger.error("Could not load LootPool #lootEntries", e);
        }

        return new ArrayList<>(0);
    }

    public static ResourceLocation getTable(LootEntryTable entryTable) {
        try {
            return ObfuscationReflectionHelper.getPrivateValue(LootEntryTable.class, entryTable, "field_186371_a"); // table
        } catch (Exception e) {
            Main.logger.error("Could not load LootEntryTable #table", e);
        }

        return new ResourceLocation("", "");
    }

    public static void setTable(LootEntryTable instance, ResourceLocation value) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(LootEntryTable.class, instance, value, "field_186371_a"); // table
        } catch (Exception e) {
            Main.logger.error("Could not modify LootEntryTable #table", e);
        }
    }

    public static void setItem(LootEntryItem instance, Item value) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(LootEntryItem.class, instance, value, "field_186368_a"); // item
        } catch (Exception e) {
            Main.logger.error("Could not modify LootEntryItem #item", e);
        }
    }

}
