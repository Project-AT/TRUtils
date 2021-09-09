package ikexing.atutils.core.block;

import ikexing.atutils.ATUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockEvilStone extends Block {
    public static final PropertyInteger STATUS = PropertyInteger.create("status", 0, 6);
    public static final String NAME = "evil_stone";

    public static final Block INSTANCE = new BlockEvilStone();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE) {
        @Override
        public int getMetadata(int damage) {
            return damage;
        }
    }.setRegistryName(NAME).setHasSubtypes(true);

    public BlockEvilStone() {
        super(Material.ROCK);
        setRegistryName(NAME);
        setTranslationKey(ATUtils.MODID + "." + NAME);
        setHardness(15.0F);
        setResistance(50.0F);
        setSoundType(SoundType.STONE);
        setLightLevel(1);
    }

    @Override
    public void getSubBlocks(@Nullable CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
        for (Integer value : STATUS.getAllowedValues()) {
            items.add(new ItemStack(this, 1, value));
        }
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        return getMetaFromState(state);
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
}
