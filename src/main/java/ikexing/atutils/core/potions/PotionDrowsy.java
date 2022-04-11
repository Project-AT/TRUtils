package ikexing.atutils.core.potions;

import ikexing.atutils.ATUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class PotionDrowsy extends Potion {

    public static final PotionDrowsy INSTANCE = new PotionDrowsy();

    public static final PotionType POTION_TYPE_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy", new PotionEffect(INSTANCE, 3600)).setRegistryName("drowsy");
    public static final PotionType POTION_TYPE_LONG_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy", new PotionEffect(INSTANCE, 9600)).setRegistryName("long_drowsy");
    public static final PotionType POTION_STRONG_TYPE_DROWSY =
            new PotionType(ATUtils.MODID + ".drowsy", new PotionEffect(INSTANCE, 3600)).setRegistryName("strong_drowsy");

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ATUtils.MODID + ":textures/gui/drowsy.png");

    public PotionDrowsy() {
        super(false, 0x6688FF);
        this.setRegistryName(ATUtils.MODID + ":drowsy");
        this.setPotionName("effect." + ATUtils.MODID + ".drowsy");
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        int duration = effect.getDuration();
        int fIndex = (duration % 16) / 2;

        mc.getTextureManager().bindTexture(TEXTURE);
        Objects.requireNonNull(mc.currentScreen).drawTexturedModalRect(
                x + 6, y + 7, fIndex * 18, 0, 18, 18);
    }

    @Override
    public void renderHUDEffect(int x, int y, @NotNull PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(TEXTURE);
        mc.ingameGUI.drawTexturedModalRect(
                x + 3, y + 3, 0, 0, 18, 18);
    }

}
