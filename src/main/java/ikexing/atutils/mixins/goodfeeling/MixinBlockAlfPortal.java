package ikexing.atutils.mixins.goodfeeling;

import ikexing.atutils.core.goodfeeling.IWanded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.advancements.AlfPortalTrigger;
import vazkii.botania.common.block.BlockAlfPortal;

import java.util.Objects;

@Mixin(value = BlockAlfPortal.class, remap = false)
public class MixinBlockAlfPortal {

    @Inject(method = "onUsedByWand", at = @At("HEAD"), cancellable = true)
    public void injectOnUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        boolean did = ((IWanded) Objects.requireNonNull(world.getTileEntity(pos))).onWanded(player, stack);
        if (!world.isRemote && did && player instanceof EntityPlayerMP) {
            AlfPortalTrigger.INSTANCE.trigger((EntityPlayerMP) player, (WorldServer) world, pos, stack);
        }
        cir.setReturnValue(did);
    }

}
