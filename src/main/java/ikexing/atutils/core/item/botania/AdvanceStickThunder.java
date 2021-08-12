package ikexing.atutils.core.item.botania;

import java.util.Objects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;

public class AdvanceStickThunder extends StickThunder {

    public static final String NAME = "advance_stick_thunder";
    public static final Item INSTANCE = new AdvanceStickThunder(NAME);

    public AdvanceStickThunder(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = (EntityPlayer) entity;
            BlockPos blockPos = Objects.requireNonNull(player.rayTrace(20, 1)).getBlockPos();
            if (ManaItemHandler.requestManaExactForTool(stack, player, 1000, true)) {
                world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), false));
                player.getCooldownTracker().setCooldown(stack.getItem(), 100);
            } else {
                player.sendMessage(new TextComponentString(I18n.translateToLocal("item.atutils.stick_thunder.message")));
            }
        }
        return stack;
    }
}
