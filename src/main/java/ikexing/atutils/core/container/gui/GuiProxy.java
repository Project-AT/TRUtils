package ikexing.atutils.core.container.gui;

import ikexing.atutils.core.container.ContainerBunker;
import ikexing.atutils.core.tile.IGuiProvider;
import ikexing.atutils.core.tile.modularmachinery.TileBunker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if(ID < 0 && te instanceof IGuiProvider) {
            return ((IGuiProvider) te).getServerGui(player);
        }
        if (te instanceof TileBunker) {
            return new ContainerBunker(player.inventory, (TileBunker) te);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if(ID < 0 && te instanceof IGuiProvider) {
            return ((IGuiProvider) te).getClientGui(player);
        }
        if (te instanceof TileBunker) {
            return new GuiBunker(new ContainerBunker(player.inventory, (TileBunker) te));
        }
        return null;
    }

}
