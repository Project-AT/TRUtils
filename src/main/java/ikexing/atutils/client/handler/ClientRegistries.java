package ikexing.atutils.client.handler;

import ikexing.atutils.ATUtils;
import ikexing.atutils.client.render.BlockOutlineRender;
import ikexing.atutils.core.block.BlockEvilStone;
import ikexing.atutils.core.block.BlockRustyIron;
import ikexing.atutils.core.block.BlockWashingMachine;
import ikexing.atutils.core.fluids.FluidAura;
import ikexing.atutils.core.item.AuthorFood;
import ikexing.atutils.core.item.CrudeSteel;
import ikexing.atutils.core.item.FlintHoe;
import ikexing.atutils.core.item.botania.GoodFeelingLevel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistries {

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        regModel(FlintHoe.INSTANCE);
        regModel(ATUtils.equivalentFuel);
        regModel(BlockEvilStone.ITEM_BLOCK);
        regModel(BlockRustyIron.ITEM_BLOCK);
        regModel(ATUtils.magneticAttraction);
        regModel(BlockWashingMachine.ITEM_BLOCK);
        regModelWithMeta(GoodFeelingLevel.INSTANCE, 6);

        AuthorFood.convert();
        BlockOutlineRender.INSTANCE.init();

        CrudeSteel.ITEMS.forEach(ClientRegistries::regModel);
        AuthorFood.ITEM_FOODS.forEach(ClientRegistries::regModel);
    }

    public static void regModelWithMeta(Item item, int maxMetadata) {
        for (int i = 0; i < maxMetadata; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }
    }

    public static void regModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap textureMap = event.getMap();
        textureMap.registerSprite(FluidAura.auraEnd.getFlowing());
        textureMap.registerSprite(FluidAura.auraNether.getFlowing());
        textureMap.registerSprite(FluidAura.auraOverworld.getFlowing());
        textureMap.registerSprite(FluidAura.auraUnderworld.getFlowing());
        textureMap.registerSprite(FluidAura.auraEnd.getStill());
        textureMap.registerSprite(FluidAura.auraNether.getStill());
        textureMap.registerSprite(FluidAura.auraOverworld.getStill());
        textureMap.registerSprite(FluidAura.auraUnderworld.getStill());
    }

}
