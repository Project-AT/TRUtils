package projectat.trutils.mixins.goodfeeling;

import projectat.trutils.core.goodfeeling.IWanded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ModSounds;

@Mixin(value = TileAlfPortal.class, remap = false)
public abstract class MixinTileAlfPortal extends TileMod implements IWanded {

    @Shadow
    protected abstract AlfPortalState getValidState();

    @Inject(method = "writePacketNBT", at = @At("RETURN"))
    public void injectWritePacketNBT(NBTTagCompound cmp, CallbackInfo ci) {
        cmp.setDouble("packetGoodFeelingExperience", getTileData().getDouble("goodFeelingExperience"));
    }

    @Inject(method = "readPacketNBT", at = @At("RETURN"))
    public void injectReadPacketNBT(NBTTagCompound cmp, CallbackInfo ci) {
        if (cmp.hasKey("packetGoodFeelingExperience"))
            getTileData().setDouble("goodFeelingExperience", cmp.getDouble("packetGoodFeelingExperience"));
    }

    @Override
    public boolean onWanded(EntityPlayer player, ItemStack wand) {
        AlfPortalState state = world.getBlockState(getPos()).getValue(BotaniaStateProps.ALFPORTAL_STATE);
        if (state == AlfPortalState.OFF) {
            AlfPortalState newState = getValidState();
            if (newState != AlfPortalState.OFF) {
                world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.ALFPORTAL_STATE, newState), 1 | 2);
                return true;
            }
            return false;
        } else {
            sendPacketToClient(player);
            return true;
        }
    }

    private void sendPacketToClient(EntityPlayer player) {
        if (!world.isRemote) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            writePacketNBT(nbttagcompound);
            double goodFeelingExperience = getTileData().getDouble("goodFeelingExperience");
            nbttagcompound.setDouble("packetGoodFeelingExperience", goodFeelingExperience == 0 ? 0.01D : goodFeelingExperience);
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketUpdateTileEntity(pos, -999, nbttagcompound));
            }
        }

        world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.dash, SoundCategory.PLAYERS, 0.11F, 1F);
    }

}
