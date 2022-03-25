package ikexing.atutils.core.item;

import ikexing.atutils.ATUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoodFeelingLevel extends Item {

    public static final String NAME = "good_feeling_level";
    public static final Item INSTANCE = new GoodFeelingLevel();

    public GoodFeelingLevel() {
        this.setRegistryName(NAME);
        this.setTranslationKey(ATUtils.MODID + "." + NAME);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(I18n.translateToLocal("item.atutils.good_feeling_level.tooltip"));
    }

}
