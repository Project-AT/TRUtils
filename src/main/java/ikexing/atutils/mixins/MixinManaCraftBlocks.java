package ikexing.atutils.mixins;

import mana_craft.init.ManaCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.common.block.ModBlocks;

@Mixin(value = ManaCraftBlocks.class, remap = false)
public class MixinManaCraftBlocks {
    @Final
    @Shadow
    @Mutable
    public static Block orichalcum_block = ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.values()[1]).getBlock();
}
