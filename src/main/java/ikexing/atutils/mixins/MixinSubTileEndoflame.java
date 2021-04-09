package ikexing.atutils.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;

@Mixin(value = SubTileEndoflame.class, remap = false)
public class MixinSubTileEndoflame {
    @Inject(method = "onUpdate", at = @At(value = "INVOKE"), cancellable = true)
    public void injectOnUpdate(CallbackInfo ci){
        System.out.println("mixins endoflame......");
        ci.cancel();
    }
}
