package projectat.trutils.core.block;

import projectat.trutils.Main;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockEvilStone extends Block {
    public static final PropertyInteger STATUS = PropertyInteger.create("status", 0, 6);
    public static final String NAME = "evil_stone";

    public static final Block INSTANCE = new BlockEvilStone();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName(NAME);

    public BlockEvilStone() {
        super(Material.ROCK);
        setRegistryName(NAME);
        setTranslationKey(Main.MODID + "." + NAME);
        setHardness(15.0F);
        setResistance(50.0F);
        setSoundType(SoundType.STONE);
        setLightLevel(1);
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        return 0;
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(STATUS);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(STATUS, meta);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATUS);
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return findAround(state, world, pos);
    }

    private IBlockState findAround(IBlockState state, IBlockAccess world, BlockPos pos) {
        Integer status = state.getValue(STATUS);
        if (status < 5) return state;
        for (EnumFacing facing : EnumFacing.VALUES) {
            IBlockState blockState = world.getBlockState(pos.offset(facing));
            if (isFive(blockState)) {
                if (isSealOff(world, pos, facing)) return state.withProperty(STATUS, 6);
            }
        }
        return state.withProperty(STATUS, 5);
    }

    private boolean isSealOff(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        for (EnumFacing value : EnumFacing.VALUES) {
            if (facing == value) continue;
            if (isFive(world.getBlockState(pos.offset(value)))) {
                return true;
            } else {
                for (EnumFacing value_ : EnumFacing.VALUES) {
                    if (pos.offset(value).offset(value_) == pos.offset(facing)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFive(IBlockState state) {
        return state.getBlock() instanceof BlockEvilStone && state.getValue(STATUS) >= 5;
    }
}
