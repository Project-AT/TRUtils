package ikexing.atutils.core.item;

import com.google.common.collect.Lists;
import ikexing.atutils.ATUtils;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CrudeSteel {

    public static final String NAME = "crude_steel";
    public static final List<Item> ITEMS = Lists.newArrayList();
    public static final ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial(NAME, 2, 380, 7.5F, 3.5F, 15);
    public static final ArmorMaterial ARMOR_MATERIAL = EnumHelper.addArmorMaterial(NAME, ATUtils.MODID + ":" + NAME, 22, new int[]{2, 5, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);

    public static final ItemArmor BOOTS = new Armor(EntityEquipmentSlot.FEET);
    public static final ItemArmor HELMET = new Armor(EntityEquipmentSlot.HEAD);
    public static final ItemArmor LEGGINGS = new Armor(EntityEquipmentSlot.LEGS);
    public static final ItemArmor CHEST_PLATE = new Armor(EntityEquipmentSlot.CHEST);

    static {
        assert TOOL_MATERIAL != null;
        ItemPickaxe pickaxe = new ItemPickaxe(TOOL_MATERIAL) {
        };
        pickaxe.setTranslationKey(ATUtils.MODID + "." + NAME + "_pickaxe");
        pickaxe.setRegistryName(NAME + "_pickaxe");

        ItemAxe axe = new ItemAxe(TOOL_MATERIAL, 8.0F, -2.9F) {
        };
        axe.setTranslationKey(ATUtils.MODID + "." + NAME + "_axe");
        axe.setRegistryName(NAME + "_axe");

        ItemSpade shovel = new ItemSpade(TOOL_MATERIAL);
        shovel.setTranslationKey(ATUtils.MODID + "." + NAME + "_shovel");
        shovel.setRegistryName(NAME + "_shovel");

        ItemHoe hoe = new ItemHoe(TOOL_MATERIAL);
        hoe.setTranslationKey(ATUtils.MODID + "." + NAME + "_hoe");
        hoe.setRegistryName(NAME + "_hoe");

        ItemSword sword = new ItemSword(TOOL_MATERIAL);
        sword.setTranslationKey(ATUtils.MODID + "." + NAME + "_sword");
        sword.setRegistryName(NAME + "_sword");

        listAddAll(pickaxe, axe, shovel, hoe, sword, BOOTS, HELMET, LEGGINGS, CHEST_PLATE);
    }

    private static void listAddAll(Item... items) {
        ITEMS.addAll(Arrays.stream(items).collect(Collectors.toList()));
    }

    public static class Armor extends ItemArmor {

        public Armor(EntityEquipmentSlot equipmentSlotIn) {
            super(Objects.requireNonNull(ARMOR_MATERIAL), 0, equipmentSlotIn);
            this.setTranslationKey(ATUtils.MODID + "." + NAME + "." + equipmentSlotIn.getName());
            this.setRegistryName(NAME + "_" + equipmentSlotIn.getName());
        }

    }

}
