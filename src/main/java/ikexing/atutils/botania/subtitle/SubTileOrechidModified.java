package ikexing.atutils.botania.subtitle;

import com.google.common.base.Predicate;
import ikexing.atutils.botania.module.ModOrechid;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ikexing.atutils.core.config.ATConfig.*;

public class SubTileOrechidModified extends SubTileFunctional {
    private static final int COST = OrechidMana;
    private static final int COST_GOG = 700;
    private static final int DELAY = OrechidDelay;
    private static final int DELAY_GOG = 2;
    private static final int RANGE = 5;
    private static final int RANGE_Y = 3;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(supertile.getWorld().isRemote || redstoneSignal > 0 || !canOperate())
            return;

        int cost = getCost();
        if(mana >= cost && ticksExisted % getDelay() == 0) {
            BlockPos coord = getCoordsToPut();
            if(coord != null) {
                ItemStack stack = getOreToPut(getWorld().getBlockState(coord));
                if(!stack.isEmpty()) {
                    Block block = Block.getBlockFromItem(stack.getItem());
                    int meta = stack.getItemDamage();
                    supertile.getWorld().setBlockState(coord, block.getStateFromMeta(meta), 1 | 2);
                    if(ConfigHandler.blockBreakParticles)
                        supertile.getWorld().playEvent(2001, coord, Block.getIdFromBlock(block) + (meta << 12));
                    supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.orechid, SoundCategory.BLOCKS, 2F, 1F);

                    mana -= cost;
                    sync();
                }
            }
        }
    }

    public ItemStack getOreToPut(IBlockState state) {
        return ModOrechid.OrechidMap.get();
    }

    private BlockPos getCoordsToPut() {
        List<BlockPos> possibleCoords = new ArrayList<>();

        for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-RANGE, -RANGE_Y, -RANGE), getPos().add(RANGE, RANGE_Y, RANGE))) {
            IBlockState state = supertile.getWorld().getBlockState(pos);
            if(state.getBlock().isReplaceableOreGen(state, supertile.getWorld(), pos, getReplaceMatcher()))
                possibleCoords.add(pos);
        }

        if(possibleCoords.isEmpty())
            return null;
        return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
    }

    public boolean canOperate() {
        return true;
    }


    public Predicate<IBlockState> getReplaceMatcher() {
        return state -> state.equals();
    }

    public int getCost() {
        return Botania.gardenOfGlassLoaded ? COST_GOG : COST;
    }

    public int getDelay() {
        return Botania.gardenOfGlassLoaded ? DELAY_GOG : DELAY;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(toBlockPos(), RANGE);
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }

    @Override
    public int getColor() {
        return 0x818181;
    }

    @Override
    public int getMaxMana() {
        return getCost();
    }

    @Override
    public LexiconEntry getEntry() {
        return LexiconData.orechid;
    }

    private static class StringRandomItem extends WeightedRandom.Item {

        public final String s;

        public StringRandomItem(int par1, String s) {
            super(par1);
            this.s = s;
        }

    }
}
