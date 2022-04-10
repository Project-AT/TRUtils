package ikexing.atutils.core.tile;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ikexing.atutils.client.gui.GuiWashingMachine;
import ikexing.atutils.core.container.ContainerWashingMachine;
import ikexing.atutils.core.crt.WashingMachineRecipe;
import ikexing.atutils.core.crt.WashingMachineRecipes;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TileWashingMachine extends TileEntity implements ITickable, IGuiProvider {
    public static final int maxEnergy = 16000;
    public static final int maxFluid = 8000;

    private int washingTime;
    private WashingMachineRecipe currentRecipe;
    private boolean needNetworkSync;
    private int tickExisted;
    private boolean needCheckRecipe;

    private final ItemStackHandler inputInventory = new SyncableItemHandler(4);
    private final ItemStackHandler outputInventory = new SyncableItemHandler(4);

    private final DynamicFluidTank inputTank = new DynamicFluidTank(maxFluid);
    private final DynamicFluidTank outputTank = new DynamicFluidTank(maxFluid);


    private int energy = 0;

    private EnumFacing direction;

    private final MachineFluidHandler fluidHandler = new MachineFluidHandler();
    private final IItemHandler itemHandler = new MachineItemHandler();
    private final IEnergyStorage energyHandler = new MachineEnergyHandler();


    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) fluidHandler;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        }
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) energyHandler;
        }
        return super.getCapability(capability, facing);
    }

    public void writeCommonNBT(NBTTagCompound compound) {
        compound.setTag("inputTank", inputTank.writeToNBT(new NBTTagCompound()));
        compound.setTag("outputTank", outputTank.writeToNBT(new NBTTagCompound()));
    }

    public void readCommonNBT(NBTTagCompound compound) {
        if (compound.hasKey("inputTank")) {
            inputTank.readFromNBT(compound.getCompoundTag("inputTank"));
        }
        if (compound.hasKey("outputTank")) {
            outputTank.readFromNBT(compound.getCompoundTag("outputTank"));
        }

    }


    public void writeSaveNBT(NBTTagCompound compound) {
        compound.setTag("inputInventory", inputInventory.serializeNBT());
        compound.setTag("outputInventory", outputInventory.serializeNBT());
        compound.setInteger("washingTime", washingTime);
        compound.setInteger("energy", energy);
    }

    public void readSaveNBT(NBTTagCompound compound) {
        if (compound.hasKey("inputInventory")) {
            inputInventory.deserializeNBT(compound.getCompoundTag("inputInventory"));
        }
        if (compound.hasKey("outputInventory")) {
            outputInventory.deserializeNBT(compound.getCompoundTag("outputInventory"));
        }
        if (compound.hasKey("washingTime")) {
            washingTime = compound.getInteger("washingTime");
        }
        if (compound.hasKey("energy")) {
            energy = compound.getInteger("energy");
        }
    }

    public void markNetSync() {
        needNetworkSync = true;
    }

    public void markRecipeCheck() {
        needCheckRecipe = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCommonNBT(compound);
        readSaveNBT(compound);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCommonNBT(compound);
        writeSaveNBT(compound);
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        writeCommonNBT(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        readCommonNBT(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, -999, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    public DynamicFluidTank getInputTank() {
        return inputTank;
    }

    public DynamicFluidTank getOutputTank() {
        return outputTank;
    }

    public EnumFacing getDirection() {
        if (direction == null) {
            direction = world.getBlockState(pos).getValue(BlockHorizontal.FACING);
        }
        return direction;
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        direction = null;
    }

    public int getEnergy() {
        return energy;
    }

    public int getWashingTime() {
        return washingTime;
    }

    public ItemStackHandler getInputInventory() {
        return inputInventory;
    }

    public ItemStackHandler getOutputInventory() {
        return outputInventory;
    }

    public void broadcastTile() {
        SPacketUpdateTileEntity packet = getUpdatePacket();
        PlayerChunkMapEntry chunk = ((WorldServer) world).getPlayerChunkMap().getEntry(getPos().getX() >> 4, getPos().getZ() >> 4);
        if (chunk != null) {
            chunk.sendPacket(packet);
        }
    }

    private boolean matchItem(WashingMachineRecipe recipe) {
        IIngredient itemInput = recipe.getItemInput();
        int slots = inputInventory.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack stack = inputInventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            IItemStack itemStack = CraftTweakerMC.getIItemStack(stack);
            if (itemInput.matches(itemStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchRecipe(WashingMachineRecipe recipe) {
        if (recipe == null) {
            return false;
        }

        if (recipe.getEnergyConsume() > getEnergy()) {
            return false;
        }

        ILiquidStack inputLiquid = CraftTweakerMC.getILiquidStack(inputTank.getFluid());
        if (!recipe.getFluidInput().matches(inputLiquid)) {
            return false;
        }

        return matchItem(recipe);
    }

    private WashingMachineRecipe findRecipe() {
        for (WashingMachineRecipe recipe : WashingMachineRecipes.recipes) {
            int totalEnergy = recipe.getEnergyConsume() * recipe.getRecipeTime();
            if (totalEnergy < getEnergy() && matchRecipe(recipe)) {
                return recipe;
            }
        }
        return null;
    }

    private boolean canOutput() {
        ItemStack itemOutput = CraftTweakerMC.getItemStack(currentRecipe.getItemOutput());
        FluidStack fluidOutput = CraftTweakerMC.getLiquidStack(currentRecipe.getFluidOutput());

        return ItemHandlerHelper.insertItem(outputInventory, itemOutput, true).isEmpty() &&
            outputTank.fill(fluidOutput, false) == fluidOutput.amount;

    }

    private void finishRecipe() {
        ItemStack itemOutput = CraftTweakerMC.getItemStack(currentRecipe.getItemOutput());
        FluidStack fluidOutput = CraftTweakerMC.getLiquidStack(currentRecipe.getFluidOutput());
        IIngredient itemInput = currentRecipe.getItemInput();
        int slots = inputInventory.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack stack = inputInventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            IItemStack itemStack = CraftTweakerMC.getIItemStack(stack);
            if (itemInput.matches(itemStack)) {
                inputInventory.extractItem(i, currentRecipe.getItemInput().getAmount(), false);
                break;
            }
        }
        inputTank.drain(currentRecipe.getFluidInput().getAmount(), true);

        ItemHandlerHelper.insertItem(outputInventory, itemOutput, false);
        outputTank.fill(fluidOutput, true);
        markRecipeCheck();
    }

    @Override
    public void update() {
        tickExisted++;
        if (!world.isRemote) {
            if (needNetworkSync) {
                needNetworkSync = false;
                broadcastTile();
            }


            if (currentRecipe == null) {
                if (tickExisted % 10 == 0)
                    currentRecipe = findRecipe();
            } else if (needCheckRecipe && !matchRecipe(currentRecipe)) {
                washingTime = 0;
                currentRecipe = null;
                markDirty();
            } else if (canOutput()) {
                washingTime++;
                energy -= currentRecipe.getEnergyConsume();
                markDirty();
                if (washingTime >= currentRecipe.getRecipeTime()) {
                    washingTime = 0;
                    finishRecipe();
                }
            } else {
                washingTime = 0;
                markDirty();
            }
        } else {
            DynamicFluidTank inputTank = getInputTank();
            FluidStack inFluid = inputTank.getFluid();
            if (inFluid != null && inFluid != inputTank.lastFluid) {
                inputTank.lastFluid = inFluid;
            }
            DynamicFluidTank outputTank = getOutputTank();
            FluidStack outFluid = outputTank.getFluid();
            if (outFluid != null && outFluid != outputTank.lastFluid) {
                outputTank.lastFluid = outFluid;
            }
        }

    }

    @Override
    public GuiContainer getClientGui(EntityPlayer player) {
        return new GuiWashingMachine(new ContainerWashingMachine(player, this));
    }

    @Override
    public Container getServerGui(EntityPlayer player) {
        return new ContainerWashingMachine(player, this);
    }

    public int getWashingTimeMax() {
        if (currentRecipe != null) {
            return currentRecipe.getRecipeTime();
        }
        return 0;
    }

    private class SyncableItemHandler extends ItemStackHandler {

        public SyncableItemHandler(int size) {
            super(size);
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            markRecipeCheck();
        }
    }

    public class DynamicFluidTank extends FluidTank {

        public DynamicFluidTank(int capacity) {
            super(capacity);
        }


        public float getActualPercentage() {
            return getFluidAmount() / (float) getCapacity();
        }

        private float lastPercentage = -1;
        private float lastTick = -1;
        private FluidStack lastFluid = null;

        public float getPercentage(float pt) {
            float dt = 0.2f;
            float ticks = tickExisted + pt;
            if (lastTick > 0) {
                dt = ticks - lastTick;
            }
            lastTick = ticks;
            if (lastPercentage >= 0 && MathHelper.abs(lastPercentage - getActualPercentage()) > 0.01) {
                lastPercentage = lastPercentage + (getActualPercentage() - lastPercentage) * dt * 0.2f;
                return lastPercentage;
            }
            lastPercentage = getActualPercentage();
            return lastPercentage;


        }

        public FluidStack getLastFluid() {
            if (lastPercentage < 0.01) {
                lastFluid = null;
            }
            return lastFluid;
        }

        @Override
        protected void onContentsChanged() {
            markDirty();
            markNetSync();
            markRecipeCheck();
        }
    }


    private class MachineItemHandler implements IItemHandler {

        @Override
        public int getSlots() {
            return 8;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot >= 4) {
                return outputInventory.getStackInSlot(slot - 4);
            }
            return inputInventory.getStackInSlot(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot >= 4) {
                return stack;
            }
            return inputInventory.insertItem(slot, stack, simulate);
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= 4) {
                return outputInventory.extractItem(slot - 4, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    }

    private class MachineFluidHandler implements IFluidHandler {

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return ArrayUtils.addAll(inputTank.getTankProperties(), outputTank.getTankProperties());
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return inputTank.fill(resource, doFill);
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return outputTank.drain(resource, doDrain);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return outputTank.drain(maxDrain, doDrain);
        }
    }

    private class MachineEnergyHandler implements IEnergyStorage {

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int energyReceived = Math.min(maxEnergy - energy, maxReceive);
            if (!simulate) {
                energy += energyReceived;
                markDirty();
            }
            return energyReceived;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return energy;
        }

        @Override
        public int getMaxEnergyStored() {
            return maxEnergy;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }
}
