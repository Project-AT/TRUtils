package ikexing.atutils.mixins;

import com.teamwizardry.wizardry.common.core.EventHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(value = EventHandler.class, remap = false)
public class MixinWizardryEventHandler {

    @Redirect(method = "playerTick", at = @At(value = "INVOKE", target = "Lcom/teamwizardry/wizardry/api/util/TeleportUtil;teleportToDimension(Lnet/minecraft/entity/player/EntityPlayer;IDDD)V", remap = false))
    private void redirectTeleportToUnderWorld(EntityPlayer player, int dimension, double x, double y, double z) {

    }
}
