package ikexing.atutils.client.jei.hydroangeas;

import ikexing.atutils.botania.module.ModHydroangeas;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HydroangeasRecipeWrapper implements IRecipeWrapper {

    public final static String S_CONSUME = I18n.format("atutils.jei.hydroangeas.consume");
    public final static String S_FACTOR = I18n.format("atutils.jei.hydroangeas.blockFactor");

    private Block input;
    private Block blockBelow;
    private double manaFactorBlock;
    private ModHydroangeas.HydroangeasHandler fluidFactorHandler;

    public HydroangeasRecipeWrapper(Map.Entry<Block, Double> entry, ModHydroangeas.HydroangeasHandler handler) {
        this.input = handler.getBlockLiquid();
        this.blockBelow = entry.getKey();
        this.manaFactorBlock = entry.getValue();
        this.fluidFactorHandler = handler;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<FluidStack> inputs = new ArrayList<>();
        inputs.add(new FluidStack(FluidRegistry.lookupFluidForBlock(input), 1000));
        inputs.add(new FluidStack(FluidRegistry.lookupFluidForBlock(ModHydroangeas.fluidFactor), 1000));
        ingredients.setInputs(VanillaTypes.FLUID, inputs);
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(Item.getItemFromBlock(blockBelow)));
    }

    protected double getManaFactorBlock() {
        return manaFactorBlock;
    }

    protected ModHydroangeas.HydroangeasHandler getFluidFactorHandler() {
        return fluidFactorHandler;
    }

}
