package projectat.trutils.core.container;

import projectat.trutils.core.tile.IFluidTankGui;
import projectat.trutils.core.tile.TileWashingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.IntSupplier;

@ParametersAreNonnullByDefault
public class ContainerWashingMachine extends Container implements IFluidTankGui {

    public ContainerWashingMachine(EntityPlayer player, TileWashingMachine washingMachine) {
        this.washingMachine = washingMachine;
        this.dataSuppliers = new IntSupplier[]
            {
                washingMachine::getEnergy,
                washingMachine::getWashingTime,
                washingMachine::getWashingTimeMax,
            };

        ItemStackHandler inputInventory = washingMachine.getInputInventory();
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(inputInventory, i, 48 + i * 18, 11));
        }

        ItemStackHandler outputInventory = washingMachine.getOutputInventory();
        for (int i = 0; i < outputInventory.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(outputInventory, i, 66 + i * 18, 58) {
                @Override
                public boolean isItemValid(@NotNull ItemStack stack) {
                    return false;
                }
            });
        }

        addPlayerSlots(player);
    }

    private final TileWashingMachine washingMachine;


    private final int[] guiData = new int[3];
    private final IntSupplier[] dataSuppliers;

    public TileWashingMachine getWashingMachine() {
        return washingMachine;
    }

    public int[] getGuiData() {
        return guiData;
    }

    public void addPlayerSlots(EntityPlayer player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getPosition().distanceSq(washingMachine.getPos()) < 64;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        for (int i = 0; i < 2; i++) {
            this.guiData[i] = this.dataSuppliers[i].getAsInt();
            listener.sendWindowProperty(this, i, this.guiData[i]);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.washingMachine != null) {
            for (int i = 0; i < 3; i++) {
                if (this.guiData[i] != this.dataSuppliers[i].getAsInt()) {
                    this.guiData[i] = this.dataSuppliers[i].getAsInt();
                    for (IContainerListener listener : this.listeners) {
                        listener.sendWindowProperty(this, i, this.guiData[i]);
                    }
                }
            }

        }
    }


    @Override
    public void updateProgressBar(int id, int data) {
        this.guiData[id] = data;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        Slot srcSlot = inventorySlots.get(slot);
        if (srcSlot == null || !srcSlot.getHasStack()) {
            return ItemStack.EMPTY;
        }
        ItemStack srcStack = srcSlot.getStack().copy();
        ItemStack copyOfSrcStack = srcStack.copy();


        if (slot < 4) {
            if (!mergeItemStack(srcStack, 8, 8 + 36, false))
                return ItemStack.EMPTY;
        } else if (slot < 8) {
            if (!mergeItemStack(srcStack, 8, 8 + 36, true))
                return ItemStack.EMPTY;
        } else {
            if (!mergeItemStack(srcStack, 0, 4, false))
                return ItemStack.EMPTY;
        }

        srcSlot.putStack(srcStack);
        srcSlot.onSlotChange(srcStack, copyOfSrcStack);
        srcSlot.onTake(player, srcStack);

        return copyOfSrcStack;
    }

    @Override
    public FluidTank getTankById(int id) {
        if (id == 0) {
            return washingMachine.getInputTank();
        }
        if (id == 1) {
            return washingMachine.getOutputTank();
        }

        return null;
    }

}

