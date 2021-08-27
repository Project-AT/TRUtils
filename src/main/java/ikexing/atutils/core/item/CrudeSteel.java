package ikexing.atutils.core.item;

import com.google.common.collect.Lists;
import ikexing.atutils.ATUtils;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CrudeSteel {

    public static final String NAME = "crude_steel";
    public static final List<Item> ITEMS = Lists.newArrayList();
    public static final ToolMaterial CRUDE_STEEL = EnumHelper.addToolMaterial("CRUDE_STEEL", 2, 380, 7.5F, 3.5F, 15);

    static {
        assert CRUDE_STEEL != null;
        ItemPickaxe pickaxe = new ItemPickaxe(CRUDE_STEEL) {};
        pickaxe.setTranslationKey(ATUtils.MODID + "." + NAME + "_pickaxe");
        pickaxe.setRegistryName(NAME + "_pickaxe");

        ItemAxe axe = new ItemAxe(CRUDE_STEEL, 3.5F, 7.5F) {};
        axe.setTranslationKey(ATUtils.MODID + "." + NAME + "_axe");
        axe.setRegistryName(NAME + "_axe");

        ItemSpade shovel = new ItemSpade(CRUDE_STEEL);
        shovel.setTranslationKey(ATUtils.MODID + "." + NAME + "_shovel");
        shovel.setRegistryName(NAME + "_shovel");

        ItemHoe hoe = new ItemHoe(CRUDE_STEEL);
        hoe.setTranslationKey(ATUtils.MODID + "." + NAME + "_hoe");
        hoe.setRegistryName(NAME + "_hoe");

        ItemSword sword = new ItemSword(CRUDE_STEEL);
        sword.setTranslationKey(ATUtils.MODID + "." + NAME + "_sword");
        sword.setRegistryName(NAME + "_sword");

        listAddAll(pickaxe, axe, shovel, hoe, sword);
    }

    private static void listAddAll(Item... items) {
        ITEMS.addAll(Arrays.stream(items).collect(Collectors.toList()));
    }

}
