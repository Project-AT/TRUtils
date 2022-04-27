package projectat.trutils.core.item;

import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import projectat.trutils.Main;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AuthorFood {

    public static final List<Item> ITEM_FOODS = new ArrayList<>();
    public static final List<AuthorInformation> AUTHOR_QQ_NUMBER = Lists.newArrayList();
    private static final String FILE = getPath(System.getProperty("user.dir"), "resources", "trutils", "textures", "items", "{0}.jpg");
    private static final String FILE_PNG = getPath(System.getProperty("user.dir"), "resources", "trutils", "textures", "items", "{0}.png");

    private static final String FILE_JSON = getPath(System.getProperty("user.dir"), "resources", "trutils", "models", "item", "{0}.json");

    private static final String JSON = "{\n" +
            "  \"parent\": \"item/generated\",\n" +
            "  \"textures\": {\n" +
            "    \"layer0\": \"trutils:items/${name}\"\n" +
            "  }\n" +
            "}";

    private static String getAvatarUrl(String number) {
        return MessageFormat.format("https://q1.qlogo.cn/g?b=qq&nk={0}&s=640", number);
    }

    private static String format(String name) {
        return I18n.format(MessageFormat.format("item.{0}.{1}.tooltip", Main.MODID, name.toLowerCase(Locale.ROOT)));
    }

    private static File getTrueName(String file, String name) {
        return FileUtil.file(MessageFormat.format(file, name));
    }

    public static void downloadAvatar() {
        try {
            for (AuthorInformation authorInfo : AUTHOR_QQ_NUMBER) {
                String name = authorInfo.getName();
                HttpUtil.downloadFile(getAvatarUrl(authorInfo.getNumber()), getTrueName(FILE, name));

                File fileJson = getTrueName(FILE_JSON, name);
                if (!FileUtil.getParent(fileJson, 1).exists()) {
                    FileUtil.mkdir(FileUtil.getParent(fileJson, 1));
                }
                if (!FileUtil.exist(fileJson)) {
                    FileUtil.writeUtf8String(JSON.replace("${name}", name), fileJson);
                }
            }
        } catch (Exception ignored) {}
    }

    public static void convert() {
        for (AuthorInformation authorInfo : AUTHOR_QQ_NUMBER) {
            Img.from(getTrueName(FILE, authorInfo.getName()))
                    .setTargetImageType(ImgUtil.IMAGE_TYPE_PNG)
                    .setQuality(-1).scale(144, 144)
                    .round(0.5).write(getTrueName(FILE_PNG, authorInfo.getName()));
            FileUtil.del(getTrueName(FILE, authorInfo.getName()));
        }
    }

    private static String getPath(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String s1 : path) {
            sb.append(sb.toString().endsWith(File.separator) ? s1 : (File.separator + s1));
        }
        return sb.toString();
    }

    public static class AuthorInformation {

        private final String number;
        private final String name;
        private final int healAmount;
        private final float saturationModifier;

        private AuthorInformation(String number, String name, int healAmount, float saturationModifier) {
            this.number = number;
            this.name = name;
            this.healAmount = healAmount;
            this.saturationModifier = saturationModifier;
        }

        public static AuthorInformation of(String number, String name, int healAmount, float saturationModifier) {
            return new AuthorInformation(number, name, healAmount, saturationModifier);
        }

        public static ItemFood of(AuthorInformation authorInformation) {
            ItemFood itemFood = new ItemFood(authorInformation.getHealAmount(), authorInformation.getSaturationModifier(), false) {
                @Override
                public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltips, @NotNull ITooltipFlag flagIn) {
                    String tooltip = format(authorInformation.getName());
                    if (!tooltip.contains(".tooltip")) {
                        tooltips.add(tooltip);
                    }
                }
            };
            itemFood.setAlwaysEdible();
            itemFood.setRegistryName(authorInformation.getName());
            itemFood.setTranslationKey(Main.MODID + "." + authorInformation.getName());
            return itemFood;
        }

        public String getNumber() {
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
