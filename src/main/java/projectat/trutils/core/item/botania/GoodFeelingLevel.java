package projectat.trutils.core.item.botania;

import projectat.trutils.Main;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class GoodFeelingLevel extends Item {

    public static final String NAME = "good_feeling_level";
    public static final Item INSTANCE = new GoodFeelingLevel();

    public GoodFeelingLevel() {
        this.setNoRepair();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setRegistryName(NAME);
        this.setTranslationKey(Main.MODID + "." + NAME);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.translateToLocal("item.trutils.good_feeling_level.tooltip"));
    }

}
