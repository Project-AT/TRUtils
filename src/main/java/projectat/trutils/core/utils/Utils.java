package projectat.trutils.core.utils;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class Utils {

    public static ImmutableList<IBlockState> getBlockStatesByOreDict(String oreDict) {
        ImmutableList.Builder<IBlockState> result = ImmutableList.builder();
        NonNullList<ItemStack> stacks = OreDictionary.getOres(oreDict);

        for (ItemStack stack : stacks) {
            Item item = stack.getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                int meta = stack.getMetadata();

                try {
                    //noinspection deprecation
                    IBlockState state = block.getStateFromMeta(meta);
                    result.add(state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.build();
    }

}
