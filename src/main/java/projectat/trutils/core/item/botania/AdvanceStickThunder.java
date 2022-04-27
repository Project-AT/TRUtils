package projectat.trutils.core.item.botania;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.mana.ManaItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AdvanceStickThunder extends StickThunder {

    public static final String NAME = "advance_stick_thunder";
    public static final Item INSTANCE = new AdvanceStickThunder(NAME);

    public AdvanceStickThunder(String name) {
        super(name);
    }

    @NotNull @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = (EntityPlayer) entity;
            BlockPos blockPos = Objects.requireNonNull(player.rayTrace(20, 1)).getBlockPos();
            if (ManaItemHandler.requestManaExactForTool(stack, player, 2500, true)) {
                world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), false));
                player.getCooldownTracker().setCooldown(stack.getItem(), 100);
            } else {
                player.sendMessage(new TextComponentString(I18n.translateToLocal("item.trutils.stick_thunder.message")));
            }
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return super.getMaxItemUseDuration(stack) / 2;
    }
}
