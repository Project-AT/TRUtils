package ikexing.atutils.mixins;

import com.teamwizardry.wizardry.proxy.CommonProxy;
import net.minecraft.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(value = CommonProxy.class, remap = false)
public class MixinWizardryCommonProxy {

    @Redirect(method = "preInit", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/DimensionManager;registerDimension(ILnet/minecraft/world/DimensionType;)V", remap = false))
    private void redirectRegisterUnderWorld(int id, DimensionType type) {

    }
}
