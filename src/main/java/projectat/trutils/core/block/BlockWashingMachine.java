package projectat.trutils.core.block;

import projectat.trutils.Main;
import projectat.trutils.core.tile.TileWashingMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockWashingMachine extends Block implements ITileEntityProvider {

    public static final String NAME = "washing_machine";

    public static final Block INSTANCE = new BlockWashingMachine();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName(NAME);

    public BlockWashingMachine() {
        super(Material.ROCK);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.setLightLevel(0.0F);

        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        this.setRegistryName(NAME);
        this.setTranslationKey(Main.MODID + "." + NAME);
    }

    public static Vec3d rotate(float hitX, float hitY, float hitZ, IBlockState state) {
        EnumFacing facing = state.getValue(BlockHorizontal.FACING);
        return new Vec3d(hitX, hitY, hitZ).subtract(0.5, 0, 0.5).rotateYaw(facing.getHorizontalAngle() * ((float) Math.PI / 180)).add(0.5, 0, 0.5);
    }

    @NotNull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @NotNull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
    }

    @NotNull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
        return this.getDefaultState().withProperty(BlockHorizontal.FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
    }

    @NotNull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @NotNull
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    @NotNull
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return super.isOpaqueCube(state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileWashingMachine();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof TileWashingMachine)) {
            return true;
        }
        TileWashingMachine washingMachine = (TileWashingMachine) tile;

        ItemStack heldItem = playerIn.getHeldItemMainhand();
        if (!heldItem.isEmpty() && FluidUtil.getFluidHandler(heldItem) != null) {
            Vec3d hit = rotate(hitX, hitY, hitZ, state);
            //Fill input tank
            if (hit.z > 0.5 && hit.x < 0.5) {
                FluidUtil.interactWithFluidHandler(playerIn, hand, washingMachine.getInputTank());
            } else if (hit.z > 0.5 && hit.x > 0.5) {
                FluidUtil.interactWithFluidHandler(playerIn, hand, washingMachine.getOutputTank());
            }
            return true;
        }

        playerIn.openGui(Main.instance, -99, worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

}
