package ikexing.atutils.mixins;

import mana_craft.item.ItemMPGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.ModBlocks;

@Mixin(value = ItemMPGenerator.class, remap = false)
public class MixinItemMPGenerator {

    @Inject(method = "buildFrame", at = @At("RETURN"), cancellable = true)
    private static void injectBuildFrame(World world, BlockPos root, EnumFacing facing, CallbackInfo ci) {
        world.setBlockState(root.add(1, 0, 1), ModBlocks.storage.getStateFromMeta(2));
        world.setBlockState(root.add(-1, 0, 1), ModBlocks.storage.getStateFromMeta(2));
        world.setBlockState(root.add(1, 0, -1), ModBlocks.storage.getStateFromMeta(2));
        world.setBlockState(root.add(-1, 0, -1), ModBlocks.storage.getStateFromMeta(2));
    }
}
