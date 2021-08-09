package ikexing.atutils.core.item;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import ikexing.atutils.ATUtils;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class AuthorFood {

    private static final String FILE = getPath(System.getProperty("user.dir"), "resources", "atutils", "textures", "items", "{0}.jpg");
    private static final String FILE_PNG = getPath(System.getProperty("user.dir"), "resources", "atutils", "textures", "items", "{0}.png");

    private static final List<Item> ITEM_FOODS = new ArrayList<>();
    private static final List<AuthorInformation> AUTHOR_QQ_NUMBER = Lists.newArrayList(
        AuthorInformation.of(963331014L, "mo", 0, 0F),
        AuthorInformation.of(651274009L, "cb", 2, 2F),
        AuthorInformation.of(3398804669L, "teddy", 16, 30F),
        AuthorInformation.of(2093615664L, "niyan", 0, 0F),
        AuthorInformation.of(825802847L, "ikexing", Integer.MAX_VALUE, Float.MAX_VALUE),
        AuthorInformation.of(1422179824L, "yangyang", 0, 0F),
        AuthorInformation.of(372395476L, "six_color", 0, 0F),
        AuthorInformation.of(3266271262L, "faulkner", 0, 0F),
        AuthorInformation.of(1377899390L, "cyciling", 0, 0F),
        AuthorInformation.of(3181063382L, "superhelo", 10, 16F),
        AuthorInformation.of(646886334L, "hakzn_anox", 0, 0F),
        AuthorInformation.of(3209636087L, "seleclipse", 16, 8F)
    );

    private static String getAvatarUrl(Long number) {
        return MessageFormat.format("https://q1.qlogo.cn/g?b=qq&nk={0}&s=640", number.toString());
    }

    private static String format(String name) {
        return I18n.format(MessageFormat.format("item.{0}.{1}.tooltip", ATUtils.MODID, name.toLowerCase(Locale.ROOT)));
    }

    private static void regModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }

    private static File getTrueName(String file, String name) {
        return FileUtil.file(MessageFormat.format(file, name));
    }

    public static void downloadAvatar() {
        AUTHOR_QQ_NUMBER.forEach(author -> HttpUtil.downloadFile(getAvatarUrl(author.getNumber()), getTrueName(FILE, author.getName())));
    }

    public static void convert() {
        AUTHOR_QQ_NUMBER.stream()
            .filter(author -> Objects.nonNull(getTrueName(FILE, author.getName())))
            .peek(author -> ImgUtil.convert(getTrueName(FILE, author.getName()), getTrueName(FILE_PNG, author.getName())))
            .forEach(author -> FileUtil.del(getTrueName(FILE, author.getName())));
    }

    @SubscribeEvent
    public static void onItemRegister(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(AUTHOR_QQ_NUMBER.stream().map(AuthorInformation::of).peek(ITEM_FOODS::add).toArray(ItemFood[]::new));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        convert();
        ITEM_FOODS.forEach(AuthorFood::regModel);
    }

    private static String getPath(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String s1 : path) {
            sb.append(sb.toString().endsWith(File.separator) ? s1 : (File.separator + s1));
        }
        return sb.toString();
    }

    private static class AuthorInformation {

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
