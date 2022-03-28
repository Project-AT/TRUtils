package ikexing.atutils.core.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import crafttweaker.api.data.IData;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.data.StringIDataParser;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.goodfeeling.IGoodFeeling;
import ink.ikx.rt.api.mods.botania.ITileAlfPortal;
import ink.ikx.rt.impl.mods.botania.event.AlfPortalDroppedEvent;
import ink.ikx.rt.impl.mods.botania.event.ElvenTradeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.recipe.RecipeElvenTrade;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class GoodFeelingEvent {

    public static Map<Integer, Integer> GOOD_FEELING_LEVEL = Maps.newHashMap();
    public static Map<BlockPos, List<ItemStack>> ALF_PORTAL_TEMP_STACK = Maps.newHashMap();

    static {
        GOOD_FEELING_LEVEL.put(0, 0);
        GOOD_FEELING_LEVEL.put(10, 1);
        GOOD_FEELING_LEVEL.put(50, 2);
        GOOD_FEELING_LEVEL.put(100, 3);
        GOOD_FEELING_LEVEL.put(500, 4);
        GOOD_FEELING_LEVEL.put(1000, 5);
    }

    @SubscribeEvent
    public static void onElvenTrade(ElvenTradeEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onAlfPortalDropped(AlfPortalDroppedEvent event) {
        ITileAlfPortal alfPortal = event.getAlfPortal();
        BlockPos blockPos = CraftTweakerMC.getBlockPos(alfPortal.getBlockPos());

        if (!ALF_PORTAL_TEMP_STACK.containsKey(blockPos)) {
            ALF_PORTAL_TEMP_STACK.put(blockPos, Lists.newArrayList());
        }

        List<ItemStack> stacks = ALF_PORTAL_TEMP_STACK.get(blockPos);

        ItemStack copy = event.getInput().getItem().copy();
        copy.setCount(1);

        for (int i = 0; i < event.getInput().getItem().getCount(); i++) {
            stacks.add(copy.copy());
        }

        stacks.removeIf(i -> ATUtils.ALF_PORTAL_EXPERIENCE.entrySet().stream().anyMatch(e -> {
            boolean toReturn = e.getKey().matches(CraftTweakerMC.getIItemStack(i));
            if (toReturn) addExperience(alfPortal, e.getValue());
            return toReturn;
        }));

        for (RecipeElvenTrade value : ATUtils.RECIPE_ELVEN_TRADES.values()) {
            while (value.matches(stacks, false)) {
                if (getLevel(getExperience(alfPortal)) >= ((IGoodFeeling) value).getGoodFeelingLevel()) {
                    value.matches(stacks, true);
                    addExperience(alfPortal, ((IGoodFeeling) value).getGoodFeelingExperience());
                    CraftTweakerMC.getIItemStackList(value.getOutputs()).forEach(alfPortal::spawnItemStack);
                } else {
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ALF_PORTAL_TEMP_STACK.remove(event.getPos());
    }

    public static void addExperience(ITileAlfPortal alfPortal, double number) {
        if (getExperience(alfPortal) + number < 0) {
            alfPortal.updateData(StringIDataParser.parse("{goodFeelingExperience: 0.0D as double}"));
        } else {
            alfPortal.updateData(StringIDataParser.parse("{goodFeelingExperience: " + (getExperience(alfPortal) + number) + "D as double}"));
        }
    }

    public static int getLevel(double experience) {
        return GOOD_FEELING_LEVEL.get(GOOD_FEELING_LEVEL.keySet().stream().mapToInt(Integer::intValue).filter(e -> e <= experience).max().orElse(0));
    }

    public static double getExperience(ITileAlfPortal alfPortal) {
        IData experience = alfPortal.getData().memberGet("goodFeelingExperience");
        if (experience == null) {
            alfPortal.updateData(StringIDataParser.parse("{goodFeelingExperience: 0.0D as double}"));
        }
        return alfPortal.getData().memberGet("goodFeelingExperience").asDouble();
    }

}
