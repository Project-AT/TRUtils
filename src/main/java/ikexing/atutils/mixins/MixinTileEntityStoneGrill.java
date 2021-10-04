package ikexing.atutils.mixins;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import primal_tech.tiles.TileEntityInventoryHelper;
import primal_tech.tiles.TileEntityStoneGrill;

@Pseudo
@Mixin(value = TileEntityStoneGrill.class, remap = false)
public abstract class MixinTileEntityStoneGrill extends TileEntityInventoryHelper {

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
        boolean empty = CraftTweakerMC.getIItemStack(stack).getOres().stream()
                .filter(o -> !o.getName().contains("ore"))
                .filter(o -> !o.getName().contains("dust"))
                .allMatch(o -> o.getName().contains("ingot"));
        if (empty) ci.cancel();
    }

}
