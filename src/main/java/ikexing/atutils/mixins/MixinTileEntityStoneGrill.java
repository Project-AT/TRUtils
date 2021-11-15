package ikexing.atutils.mixins;

import ikexing.atutils.ATUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import primal_tech.tiles.TileEntityInventoryHelper;
import primal_tech.tiles.TileEntityStoneGrill;

import javax.annotation.ParametersAreNonnullByDefault;

@Pseudo
@Mixin(value = TileEntityStoneGrill.class, remap = false)
public abstract class MixinTileEntityStoneGrill extends TileEntityInventoryHelper {

    @Shadow
    @ParametersAreNonnullByDefault
    public abstract boolean canExtractItem(int index, ItemStack stack, EnumFacing direction);

    protected MixinTileEntityStoneGrill(int invtSize) {
        super(invtSize);
    }

    @Inject(method = "smeltItem",
            at = @At(value = "INVOKE",
                    target = "Lprimal_tech/tiles/TileEntityStoneGrill;canSmelt()Z",
                    shift = At.Shift.AFTER),
            cancellable = true)
    public void smeltItem(CallbackInfo ci) {
        ItemStack stack = getItems().get(0);
        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(stack);
        if (ATUtils.isCancel(output.copy())) ci.cancel();
    }

}
