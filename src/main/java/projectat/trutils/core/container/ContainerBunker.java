package projectat.trutils.core.container;

import projectat.trutils.core.tile.modularmachinery.TileBunker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContainerBunker extends Container {

    private int isWork;
    public final TileBunker bunker;

    public ContainerBunker(IInventory playerInventory, TileBunker bunker) {
        this.bunker = bunker;

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    public int isWork() {
        return isWork;
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = bunker.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int slotIndex = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                int x = 26 + col * 18;
                int y = row * 18 + 25;
                this.addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int x = 98 + col * 18;
                int y = row * 18 + 16;
                this.addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
            }
        }

    }

    private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stackTemp = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            stackTemp = stack.copy();

            if (index < TileBunker.SIZE) {
                if (!this.mergeItemStack(stack, TileBunker.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, 0, TileBunker.INPUT_SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return stackTemp;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return bunker.canInteractWith(playerIn);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        TileEntity te = bunker.getWorld().getTileEntity(bunker.getPos());
        if (te instanceof TileBunker) {
            int isWork = ((TileBunker) te).isWork();
            if (isWork != this.isWork) {
                this.isWork = isWork;
                for (IContainerListener listener : this.listeners) {
                    listener.sendWindowProperty(this, 0, isWork);
                }
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) this.isWork = data;
    }

}
