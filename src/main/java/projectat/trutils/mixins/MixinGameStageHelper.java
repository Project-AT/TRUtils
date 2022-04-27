package projectat.trutils.mixins;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = GameStageHelper.class, remap = false)
public class MixinGameStageHelper {

    @Inject(method = "hasStage(Lnet/minecraft/entity/player/EntityPlayer;Ljava/lang/String;)Z", at = @At("HEAD"), cancellable = true)
    private static void injectHasStage(EntityPlayer player, String stage, CallbackInfoReturnable<Boolean> info) {
        if (player instanceof FakePlayer) info.setReturnValue(true);
    }

}
