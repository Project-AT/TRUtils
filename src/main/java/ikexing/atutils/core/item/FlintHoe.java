package ikexing.atutils.core.item;

import ikexing.atutils.ATUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Objects;

public class FlintHoe extends ItemHoe {

    public static final ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial("flint", 1, 145, 4.2F, 1.5F, 5);

    public static final String NAME = "flint_hoe";
    public static final Item INSTANCE = new FlintHoe();


    public FlintHoe() {
        super(Objects.requireNonNull(TOOL_MATERIAL));
        this.setRegistryName(NAME);
        this.setTranslationKey(ATUtils.MODID + "." + NAME);
    }

}
