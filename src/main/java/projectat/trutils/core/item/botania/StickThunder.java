package projectat.trutils.core.item.botania;

import org.jetbrains.annotations.NotNull;
import projectat.trutils.Main;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.core.BotaniaCreativeTab;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class StickThunder extends Item implements IManaUsingItem, IModelRegister {

    public static final String NAME = "stick_thunder";
    public static final Item INSTANCE = new StickThunder(NAME);

    public StickThunder(String name) {
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setRegistryName(name);
        this.setTranslationKey(Main.MODID + "." + name);
        this.setCreativeTab(BotaniaCreativeTab.INSTANCE);
    }

    @NotNull @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = (EntityPlayer) entity;
            if (ManaItemHandler.requestManaExactForTool(stack, player, 2500, true)) {
                world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, false));
                player.getCooldownTracker().setCooldown(stack.getItem(), 50);
            } else {
                player.sendMessage(new TextComponentString(I18n.translateToLocal("item.trutils.stick_thunder.message")));
            }
        }
        return stack;
    }

    @NotNull @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote)
            return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @NotNull @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 15;
    }

    @Override
    public boolean usesMana(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }
}
