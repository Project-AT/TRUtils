package projectat.trutils.core.crt;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenRegister
@ZenClass("mods.trutils.WashingMachineRecipe")
public class WashingMachineRecipe {

    private final int recipeTime;
    private final int energyConsume;
    private final IIngredient fluidInput;
    private final IIngredient itemInput;
    private final IItemStack itemOutput;
    private final ILiquidStack fluidOutput;

    public WashingMachineRecipe(int recipeTime, int energyConsume, IIngredient fluidInput, IIngredient itemInput,
            IItemStack itemOutput, ILiquidStack fluidOutput) {
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
