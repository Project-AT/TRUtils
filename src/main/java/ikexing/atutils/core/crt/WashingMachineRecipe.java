package ikexing.atutils.core.crt;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

import java.util.List;

@ZenRegister
@ZenClass("mods.atutils.WashingMachineRecipe")
public class WashingMachineRecipe {
    private int recipeTime;
    private int energyConsume;
    private IIngredient fluidInput;
    private IIngredient itemInput;
    private IItemStack itemOutput;
    private ILiquidStack fluidOutput;

    public WashingMachineRecipe(int recipeTime, int energyConsume,  IIngredient fluidInput, IIngredient itemInput, IItemStack itemOutput, ILiquidStack fluidOutput) {
        this.recipeTime = recipeTime;
        this.energyConsume = energyConsume;
        this.fluidInput = fluidInput;
        this.itemInput = itemInput;
        this.itemOutput = itemOutput;
        this.fluidOutput = fluidOutput;
    }

    @ZenGetter("recipeTime")
    public int getRecipeTime() {
        return recipeTime;
    }

    @ZenGetter("energyConsume")
    public int getEnergyConsume() {
        return energyConsume;
    }


    @ZenGetter("fluidInput")
    public IIngredient getFluidInput() {
        return fluidInput;
    }

    @ZenGetter("itemInput")
    public IIngredient getItemInput() {
        return itemInput;
    }

    @ZenGetter("itemOutput")
    public IItemStack getItemOutput() {
        return itemOutput;
    }

    @ZenGetter("fluidOutput")
    public ILiquidStack getFluidOutput() {
        return fluidOutput;
    }

}
