package ikexing.atutils.core.item;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import ikexing.atutils.ATUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AuthorFood {

    private static final String FILE = getPath(System.getProperty("user.dir"), "resources", "atutils", "textures", "items", "{0}.jpg");
    private static final String FILE_PNG = getPath(System.getProperty("user.dir"), "resources", "atutils", "textures", "items", "{0}.png");

    public static final List<Item> ITEM_FOODS = new ArrayList<>();
    public static final List<AuthorInformation> AUTHOR_QQ_NUMBER = Lists.newArrayList(
        AuthorInformation.of(963331014L, "mo", 1, 400F),
        AuthorInformation.of(651274009L, "cb", 2, 0.5F),
        AuthorInformation.of(3398804669L, "teddy", 8, 1.875F),
        AuthorInformation.of(1422179824L, "yangyang", 2, 0.5F),
        AuthorInformation.of(825802847L, "ikexing", 20, 1.0F),
        AuthorInformation.of(3181063382L, "superhelo", 5, 1.6F),
        AuthorInformation.of(3209636087L, "seleclipse", 16, 0.25F),
        AuthorInformation.of(2093615664L, "niyan", 20, 1.0F),
        AuthorInformation.of(372395476L, "six_color", 16, 0.1875F),
        AuthorInformation.of(1377899390L, "cyciling", 40, 0F),
        AuthorInformation.of(646886334L, "hakzn_anox", 4, 2.5F),
        AuthorInformation.of(3266271262L, "faulkner", 8, 0F)
    );

    private static String getAvatarUrl(Long number) {
        return MessageFormat.format("https://q1.qlogo.cn/g?b=qq&nk={0}&s=640", number.toString());
    }

    private static String format(String name) {
        return I18n.format(MessageFormat.format("item.{0}.{1}.tooltip", ATUtils.MODID, name.toLowerCase(Locale.ROOT)));
    }

    private static File getTrueName(String file, String name) {
        return FileUtil.file(MessageFormat.format(file, name));
    }

    public static void downloadAvatar() {
        try {
            AUTHOR_QQ_NUMBER.forEach(author -> HttpUtil.downloadFile(getAvatarUrl(author.getNumber()), getTrueName(FILE, author.getName())));
        } catch (Exception ignored) {
        }
    }

    public static void convert() {
        AUTHOR_QQ_NUMBER.stream()
            .filter(author -> Objects.nonNull(getTrueName(FILE, author.getName())))
            .peek(author -> ImgUtil.convert(getTrueName(FILE, author.getName()), getTrueName(FILE_PNG, author.getName())))
            .peek(author -> ImgUtil.scale(getTrueName(FILE_PNG, author.getName()), getTrueName(FILE_PNG, author.getName()), 144, 144, null))
            .forEach(author -> FileUtil.del(getTrueName(FILE, author.getName())));
    }

    private static String getPath(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String s1 : path) {
            sb.append(sb.toString().endsWith(File.separator) ? s1 : (File.separator + s1));
        }
        return sb.toString();
    }

    public static class AuthorInformation {

        private final long number;
        private final String name;
        private final int healAmount;
        private final float saturationModifier;

        private AuthorInformation(long number, String name, int healAmount, float saturationModifier) {
            this.number = number;
            this.name = name;
            this.healAmount = healAmount;
            this.saturationModifier = saturationModifier;
        }

        public static AuthorInformation of(long number, String name, int healAmount, float saturationModifier) {
            return new AuthorInformation(number, name, healAmount, saturationModifier);
        }

        public static ItemFood of(AuthorInformation authorInformation) {
            ItemFood itemFood = new ItemFood(authorInformation.getHealAmount(), authorInformation.getSaturationModifier(), false) {
                @Override
                public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltips, ITooltipFlag flagIn) {
                    String tooltip = format(authorInformation.getName());
                    if (!tooltip.contains(".tooltip")) {
                        tooltips.add(tooltip);
                    }
                }
            };
            itemFood.setAlwaysEdible();
            itemFood.setRegistryName(authorInformation.getName());
            itemFood.setTranslationKey(ATUtils.MODID + "." + authorInformation.getName());
            return itemFood;
        }

        public long getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public int getHealAmount() {
            return healAmount;
        }

        public float getSaturationModifier() {
            return saturationModifier;
        }
    }

}
