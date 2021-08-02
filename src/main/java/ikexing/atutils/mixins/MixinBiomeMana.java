package ikexing.atutils.mixins;

import mana_craft.init.ManaCraftBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = ManaCraftBiomes.class, remap = false)
public class MixinBiomeMana {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void InjectInit(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "preInit", at = @At("HEAD"), cancellable = true)
    private static void InjectPreInit(CallbackInfo ci) {
        ci.cancel();
    }
}
