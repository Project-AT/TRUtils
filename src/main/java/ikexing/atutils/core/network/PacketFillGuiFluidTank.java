package ikexing.atutils.core.network;

import hellfirepvp.modularmachinery.common.container.ContainerFluidHatch;
import hellfirepvp.modularmachinery.common.tiles.base.TileFluidTank;
import ikexing.atutils.core.tile.IFluidTankGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class PacketFillGuiFluidTank implements IMessage, IMessageHandler<PacketFillGuiFluidTank, IMessage> {
    public int tankIndex;
    public int mouseButton;

    @Override
    public void fromBytes(ByteBuf buf) {
        tankIndex = buf.readInt();
        mouseButton = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tankIndex);
        buf.writeInt(mouseButton);
    }

    public PacketFillGuiFluidTank() {
    }

    public PacketFillGuiFluidTank(int tankIndex, int mouseButton) {
        this.tankIndex = tankIndex;
        this.mouseButton = mouseButton;
    }

    @Override
    public IMessage onMessage(PacketFillGuiFluidTank message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                EntityPlayer player = ctx.getServerHandler().player;
                if (player.openContainer instanceof IFluidTankGui) {
                    ItemStack holding = player.inventory.getItemStack();
                    if (!holding.isEmpty()) {
                        FluidTank tank = ((IFluidTankGui) player.openContainer).getTankById(message.tankIndex);
                        IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                        if (tank != null && playerInventory != null) {
                            FluidActionResult result;
                            if (message.mouseButton == 0) {
                                result = FluidUtil.tryFillContainerAndStow(holding, tank, playerInventory, Integer.MAX_VALUE, player, true);
                                if (!result.isSuccess()) {
                                    result = FluidUtil.tryEmptyContainerAndStow(holding, tank, playerInventory, Integer.MAX_VALUE, player, true);
                                }
                            } else {
                                result = FluidUtil.tryEmptyContainerAndStow(holding, tank, playerInventory, Integer.MAX_VALUE, player, true);
                                if (!result.isSuccess()) {
                                    result = FluidUtil.tryFillContainerAndStow(holding, tank, playerInventory, Integer.MAX_VALUE, player, true);
                                }
                            }
                            if (result.isSuccess()) {
                                player.inventory.setItemStack(result.getResult());
                            }
                        }
                    }
                }
            });
        }
        return null;
    }
}
