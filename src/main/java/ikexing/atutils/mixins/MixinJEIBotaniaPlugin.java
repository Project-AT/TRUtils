package ikexing.atutils.mixins;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.*;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.client.gui.crafting.ContainerCraftingHalo;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeCategory;
import vazkii.botania.client.integration.jei.brewery.BreweryRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.AncientWillRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.CompositeLensRecipeWrapper;
import vazkii.botania.client.integration.jei.crafting.SpecialFloatingFlowerWrapper;
import vazkii.botania.client.integration.jei.crafting.TerraPickTippingRecipeWrapper;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeCategory;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeWrapper;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeCategory;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeWrapper;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeWrapper;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeCategory;
import vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeWrapper;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeCategory;
import vazkii.botania.client.integration.jei.runicaltar.RunicAltarRecipeWrapper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;
import static vazkii.botania.common.lib.LibBlockNames.SUBTILE_PUREDAISY;

@Mixin(value = JEIBotaniaPlugin.class, remap = false)
public class MixinJEIBotaniaPlugin {
    /**
     * @author ikexing
     * @reason ...
     */
    @Overwrite
    public void register(@Nonnull IModRegistry registry) {
        registry.handleRecipes(RecipeBrew.class, BreweryRecipeWrapper::new, BreweryRecipeCategory.UID);
        registry.handleRecipes(RecipePureDaisy.class, PureDaisyRecipeWrapper::new, PureDaisyRecipeCategory.UID);
        registry.handleRecipes(RecipeRuneAltar.class, RunicAltarRecipeWrapper::new, RunicAltarRecipeCategory.UID); // Runic must come before petals. See williewillus/Botania#172
        registry.handleRecipes(RecipePetals.class, PetalApothecaryRecipeWrapper::new, PetalApothecaryRecipeCategory.UID);
        registry.handleRecipes(RecipeElvenTrade.class, ElvenTradeRecipeWrapper::new, ElvenTradeRecipeCategory.UID);
        registry.handleRecipes(RecipeManaInfusion.class, ManaPoolRecipeWrapper::new, ManaPoolRecipeCategory.UID);

        registry.handleRecipes(SpecialFloatingFlowerRecipe.class, SpecialFloatingFlowerWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(AncientWillRecipe.class, AncientWillRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(TerraPickTippingRecipe.class, TerraPickTippingRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(CompositeLensRecipe.class, r -> new CompositeLensRecipeWrapper(), VanillaRecipeCategoryUid.CRAFTING);

        registry.addRecipes(BotaniaAPI.brewRecipes, BreweryRecipeCategory.UID);
        registry.addRecipes(BotaniaAPI.pureDaisyRecipes, PureDaisyRecipeCategory.UID);
        registry.addRecipes(BotaniaAPI.petalRecipes, PetalApothecaryRecipeCategory.UID);
        registry.addRecipes(BotaniaAPI.elvenTradeRecipes, ElvenTradeRecipeCategory.UID);
        registry.addRecipes(BotaniaAPI.runeAltarRecipes, RunicAltarRecipeCategory.UID);
        registry.addRecipes(BotaniaAPI.manaInfusionRecipes, ManaPoolRecipeCategory.UID);

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.brewery), BreweryRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.alfPortal), ElvenTradeRecipeCategory.UID);

        for(PoolVariant v : PoolVariant.values()) {
            registry.addRecipeCatalyst(new ItemStack(ModBlocks.pool, 1, v.ordinal()), ManaPoolRecipeCategory.UID);
        }

        for(AltarVariant v : AltarVariant.values()) {
            if(v == AltarVariant.MOSSY) continue;
            registry.addRecipeCatalyst(new ItemStack(ModBlocks.altar, 1, v.ordinal()), PetalApothecaryRecipeCategory.UID);
        }

        registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(SUBTILE_PUREDAISY), PureDaisyRecipeCategory.UID);
        registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), SUBTILE_PUREDAISY), PureDaisyRecipeCategory.UID);


        registry.addRecipeCatalyst(new ItemStack(ModBlocks.runeAltar), RunicAltarRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModItems.autocraftingHalo), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCatalyst(new ItemStack(ModItems.craftingHalo), VanillaRecipeCategoryUid.CRAFTING);

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCraftingHalo.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);

    }
}
