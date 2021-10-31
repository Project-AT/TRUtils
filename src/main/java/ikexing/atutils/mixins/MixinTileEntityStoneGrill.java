package ikexing.atutils.mixins;

import crafttweaker.api.minecraft.CraftTweakerMC;
import java.util.Map;
import java.util.Objects;
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

@Pseudo
@Mixin(value = TileEntityStoneGrill.class, remap = false)
public abstract class MixinTileEntityStoneGrill extends TileEntityInventoryHelper {

    @Shadow
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
        Map<ItemStack, ItemStack> smeltingMap = FurnaceRecipes.instance().getSmeltingList();
        ItemStack output = smeltingMap.get(stack);
        boolean empty = CraftTweakerMC.getIItemStack(stack).getOres().stream()
            .filter(o -> !o.getName().contains("ore"))
            .filter(o -> !o.getName().contains("dust"))
            .allMatch(o -> o.getName().contains("ingot"));

        boolean logWood = CraftTweakerMC.getIItemStack(stack).getOres().stream()
            .anyMatch(o -> o.getName().equals("logWood"));

        boolean rockOre = false;
        if (Objects.nonNull(output)) {
            rockOre = CraftTweakerMC.getIItemStack(output).getOres().stream()
                .allMatch(o -> o.getName().contains("nugget"));
        }

        if (empty || logWood || rockOre) {
            ci.cancel();
        }
    }

}
