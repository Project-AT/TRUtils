package ikexing.atutils.core.tile.modularmachinery;

import hellfirepvp.modularmachinery.common.tiles.TileItemInputBus;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import ikexing.atutils.ATUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TileBunker extends TileColorableMachineComponent implements ITickable {

    public static final int INPUT_SLOTS = 4;
    public static final int OUTPUT_SLOTS = 9;
    public static final int SIZE = INPUT_SLOTS + OUTPUT_SLOTS;

    private int burnTimeCache;

    private final ItemStackHandler inputInventory = new ItemStackHandler(INPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            TileBunker.this.markDirty();
        }
    };
    private final ItemStackHandler outputInventory = new ItemStackHandler(OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            TileBunker.this.markDirty();
        }
    };

    private final CombinedInvWrapper combinedInventory = new CombinedInvWrapper(inputInventory, outputInventory);

    @Override
    public void update() {
        if (!world.isRemote) {
            attemptStart();
            findMachineInput();
        }
    }

    private void findMachineInput() {
        for (EnumFacing value : EnumFacing.VALUES) {
            TileEntity te = world.getTileEntity(pos.offset(value));
            if (te instanceof TileItemInputBus) {
                TileItemInputBus inputBus = (TileItemInputBus) te;
                outer:
                for (int i = 0; i < inputBus.getInventory().getSlots(); i++) {
                    for (int n = 0; n < OUTPUT_SLOTS; n++) {
                        if (!outputInventory.getStackInSlot(n).isEmpty()) {
                            ItemStack stack = inputBus.getInventory().insertItem(i, new ItemStack(ATUtils.equivalentFuel, 1), false);
                            if (stack.isEmpty()) {
                                outputInventory.extractItem(n, 1, false);
                                break outer;
                            }
                        }
                    }
                }
            }
        }
    }

    private void attemptStart() {
        boolean flag = false;
        for (int i = 0; i < INPUT_SLOTS; i++) {
            ItemStack stackInSlot = inputInventory.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                int fuel = getFuel(stackInSlot);
                this.burnTimeCache += fuel;
                if (burnTimeCache >= 200) {
                    if (insertOutput(new ItemStack(ATUtils.equivalentFuel, getOutputAmount()))) {
                        inputInventory.extractItem(i, 1, false);
                        markDirty();
                        flag = !flag;
                    }
                }
            }
        }
        if (burnTimeCache >= 200 && !flag && outputInventory.getStackInSlot(OUTPUT_SLOTS - 1).getCount() != 64) {
            insertOutput(new ItemStack(ATUtils.equivalentFuel, getOutputAmount()));
            markDirty();
        }
    }

    private boolean insertOutput(ItemStack output) {
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            ItemStack stack = outputInventory.insertItem(i, output, false);
            if (stack.isEmpty()) return true;
        }
        return false;
    }

    private int getOutputAmount() {
        int res = 0;
        while (burnTimeCache >= 200 && res != 64) {
            burnTimeCache -= 200;
            res++;
        }
        if (burnTimeCache < 0) burnTimeCache += 200;
        return res;
    }

    private int getFuel(ItemStack stack) {
        return stack.getItem() != Items.LAVA_BUCKET ? TileEntityFurnace.getItemBurnTime(stack) : 0;
    }

    public CombinedInvWrapper getInventory() {
        return combinedInventory;
    }

    public int getBurnTimeCache() {
        return burnTimeCache;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedInventory);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        this.burnTimeCache = compound.getInteger("BurnTimeCache");
        this.inputInventory.deserializeNBT(compound.getCompoundTag("InputInventory"));
        this.outputInventory.deserializeNBT(compound.getCompoundTag("OutputInventory"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setInteger("BurnTimeCache", this.burnTimeCache);
        compound.setTag("InputInventory", this.inputInventory.serializeNBT());
        compound.setTag("OutputInventory", this.outputInventory.serializeNBT());
    }

}
