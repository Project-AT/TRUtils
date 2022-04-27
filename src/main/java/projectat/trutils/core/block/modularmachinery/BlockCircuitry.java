package projectat.trutils.core.block.modularmachinery;

import hellfirepvp.modularmachinery.common.CommonProxy;
import hellfirepvp.modularmachinery.common.block.BlockCustomName;
import hellfirepvp.modularmachinery.common.block.BlockMachineComponent;
import hellfirepvp.modularmachinery.common.block.BlockVariants;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockCircuitry extends BlockMachineComponent implements BlockCustomName, BlockVariants {

    private static final PropertyEnum<CircuitryType> CIRCUITRY = PropertyEnum.create("circuitry", CircuitryType.class);

    public BlockCircuitry() {
        super(Material.IRON);
        setHardness(2F);
        setResistance(10F);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(CommonProxy.creativeTabModularMachinery);
    }

    @Override
    public void getSubBlocks(@Nonnull CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
        for (CircuitryType type : CircuitryType.values()) {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(CIRCUITRY).ordinal();
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CIRCUITRY, CircuitryType.values()[MathHelper.clamp(meta, 0, CircuitryType.values().length - 1)]);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CIRCUITRY);
    }

    @Override
    public String getIdentifierForMeta(int meta) {
        return CircuitryType.values()[MathHelper.clamp(meta, 0, CircuitryType.values().length - 1)].getName();
    }

    @Override
    public Iterable<IBlockState> getValidStates() {
        List<IBlockState> ret = new LinkedList<>();
        for (CircuitryType type : CircuitryType.values()) {
            ret.add(getDefaultState().withProperty(CIRCUITRY, type));
        }
        return ret;
    }

    @Override
    public String getBlockStateName(IBlockState state) {
        return state.getValue(CIRCUITRY).getName();
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(@Nonnull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    public enum CircuitryType implements IStringSerializable {
        ADVANCED,
        ELITE,
        ULTIMATE;

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

}
