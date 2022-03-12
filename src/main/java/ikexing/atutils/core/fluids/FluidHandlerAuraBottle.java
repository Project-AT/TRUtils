package ikexing.atutils.core.fluids;

import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.items.ItemAuraBottle;
import de.ellpeck.naturesaura.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidHandlerAuraBottle implements IFluidHandlerItem, ICapabilityProvider {
    @Nonnull
    protected ItemStack container;

    public static final int BOTTLE_AMOUNT = 100;

    public FluidHandlerAuraBottle(@Nonnull ItemStack container) {
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemStack getContainer() {
        return container;
    }

    public boolean canFillFluidType(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        return fluid instanceof FluidAura;
    }

    @javax.annotation.Nullable
    public FluidStack getFluid() {
        Item item = container.getItem();
        if (!(item instanceof ItemAuraBottle)) {
            return null;
        }

        IAuraType type = ItemAuraBottle.getType(container);

        Fluid fluid = FluidAura.fluidsMapping.get(type);
        if(fluid == null) {
            return null;
        }

        return new FluidStack(fluid, BOTTLE_AMOUNT);

    }

    /**
     * @deprecated use the NBT-sensitive version {@link #setFluid(FluidStack)}
     */
    @Deprecated
    protected void setFluid(@javax.annotation.Nullable Fluid fluid) {
        setFluid(new FluidStack(fluid, BOTTLE_AMOUNT));
    }

    protected void setFluid(@javax.annotation.Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            container = new ItemStack(ModItems.BOTTLE_TWO);
            return;
        }

        Fluid fluid = fluidStack.getFluid();
        if(!(fluid instanceof FluidAura)) {
            container = new ItemStack(ModItems.BOTTLE_TWO);
            return;
        }
        IAuraType auraType = ((FluidAura) fluid).getAuraType();
        container = ItemAuraBottle.setType(new ItemStack(ModItems.AURA_BOTTLE), auraType);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new FluidTankProperties[]{new FluidTankProperties(getFluid(), BOTTLE_AMOUNT)};
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (container.getCount() != 1 || resource == null || resource.amount < BOTTLE_AMOUNT || container.getItem() != ModItems.BOTTLE_TWO || getFluid() != null || !canFillFluidType(resource)) {
            return 0;
        }

        if (doFill) {
            setFluid(resource);
        }

        return BOTTLE_AMOUNT;
    }

    @javax.annotation.Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (container.getCount() != 1 || resource == null || resource.amount < BOTTLE_AMOUNT) {
            return null;
        }

        FluidStack fluidStack = getFluid();
        if (fluidStack != null && fluidStack.isFluidEqual(resource)) {
            if (doDrain) {
                setFluid((FluidStack) null);
            }
            return fluidStack;
        }

        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (container.getCount() != 1 || maxDrain < BOTTLE_AMOUNT) {
            return null;
        }

        FluidStack fluidStack = getFluid();
        if (fluidStack != null) {
            if (doDrain) {
                setFluid((FluidStack) null);
            }
            return fluidStack;
        }

        return null;
    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        return null;
    }
}
