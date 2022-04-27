package projectat.trutils.mixins;

import hellfirepvp.modularmachinery.common.registry.RegistryBlocks;
import projectat.trutils.Main;
import projectat.trutils.core.block.modularmachinery.BlockBunker;
import projectat.trutils.core.block.modularmachinery.BlockCircuitry;
import projectat.trutils.core.tile.modularmachinery.TileBunker;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = RegistryBlocks.class, remap = false)
public class MixinRegistryBlocks {

    @Shadow
    private static <T extends Block> T prepareRegister(T block) {
        return null;
    }

    @Shadow
    private static void registerTile(Class<? extends TileEntity> tile) {}

    @Shadow
    private static void prepareItemBlockRegister(Block block) {}

    @Inject(method = "registerBlocks", at = @At(value = "HEAD"))
    private static void injectRegistryBlocks(CallbackInfo ci) {
        Main.bunker = prepareRegister(new BlockBunker());
        Main.circuitry = prepareRegister(new BlockCircuitry());
        prepareItemBlockRegister(Main.bunker);
        prepareItemBlockRegister(Main.circuitry);
    }

    @Inject(method = "registerTiles", at = @At(value = "HEAD"))
    private static void injectRegistryTiles(CallbackInfo ci) {
        registerTile(TileBunker.class);
    }
}
