package projectat.trutils.mixins;

import com.teamwizardry.wizardry.api.ConfigValues;
import com.teamwizardry.wizardry.common.world.WorldGenManaLake;
import com.teamwizardry.wizardry.common.world.WorldGeneratorWisdomTree;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Pseudo
@Mixin(value = WorldGenManaLake.class, remap = false)
public class MixinWorldGenManaLake {

    @Shadow
    private Block block;

    /**
     * @author ikx
     * @reason Best of a bad bunch
     */
    @Overwrite
    public void generate(World world, Random rand, BlockPos position) {
        int chance = rand.nextInt(ConfigValues.manaPoolRarity);
        if (chance == 0) {
            if (position.getY() > 4) {
                position = position.down(4);
                boolean[] aboolean = new boolean[2048];
                int i = rand.nextInt(4) + 4;

                for (int j = 0; j < i; ++j) {
                    double d0 = rand.nextDouble() * 6.0D + 3.0D;
                    double d1 = rand.nextDouble() * 4.0D + 2.0D;
                    double d2 = rand.nextDouble() * 6.0D + 3.0D;
                    double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                    double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                    double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                    for (int l = 1; l < 15; ++l) {
                        for (int i1 = 1; i1 < 15; ++i1) {
                            for (int j1 = 1; j1 < 7; ++j1) {
                                double d6 = ((double) l - d3) / (d0 / 2.0D);
                                double d7 = ((double) j1 - d4) / (d1 / 2.0D);
                                double d8 = ((double) i1 - d5) / (d2 / 2.0D);
                                double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                                if (d9 < 1.0D) {
                                    aboolean[(l * 16 + i1) * 8 + j1] = true;
                                }
                            }
                        }
                    }
                }

                for (int k1 = 0; k1 < 16; ++k1) {
                    for (int l2 = 0; l2 < 16; ++l2) {
                        for (int k = 0; k < 8; ++k) {
                            boolean flag = !aboolean[(k1 * 16 + l2) * 8 + k] && (k1 < 15 && aboolean[((k1 + 1) * 16 + l2) * 8 + k] || k1 > 0 && aboolean[((k1 - 1) * 16 + l2) * 8 + k] || l2 < 15 && aboolean[(k1 * 16 + l2 + 1) * 8 + k] || l2 > 0 && aboolean[(k1 * 16 + (l2 - 1)) * 8 + k] || k < 7 && aboolean[(k1 * 16 + l2) * 8 + k + 1] || k > 0 && aboolean[(k1 * 16 + l2) * 8 + (k - 1)]);

                            if (flag) {
                                Material material = world.getBlockState(position.add(k1, k, l2)).getMaterial();

                                if (k >= 4 && material.isLiquid()) {
                                    return;
                                }

                                if (k < 4 && !material.isSolid() && world.getBlockState(position.add(k1, k, l2)).getBlock() != this.block) {
                                    return;
                                }
                            }
                        }
                    }
                }

                for (int l1 = 0; l1 < 16; ++l1) {
                    for (int i3 = 0; i3 < 16; ++i3) {
                        for (int i4 = 0; i4 < 8; ++i4) {
                            if (aboolean[(l1 * 16 + i3) * 8 + i4]) {
                                BlockPos add = position.add(l1, i4, i3);
                                IBlockState state = i4 >= 4 ? Blocks.AIR.getDefaultState() : this.block.getDefaultState();
                                world.setBlockState(add, state, 2);
                                if (state.equals(this.block.getDefaultState())) {
                                    BlockPos lowestSpot = IAuraChunk.getLowestSpot(world, add, 4, add);
                                    IAuraChunk.getAuraChunk(world, lowestSpot).storeAura(lowestSpot, 8000);
                                }
                            }
                        }
                    }
                }

                for (int i2 = 0; i2 < 16; ++i2) {
                    for (int j3 = 0; j3 < 16; ++j3) {
                        for (int j4 = 4; j4 < 8; ++j4) {
                            if (aboolean[(i2 * 16 + j3) * 8 + j4]) {
                                BlockPos blockpos = position.add(i2, j4 - 1, j3);

                                if (world.getBlockState(blockpos).getBlock() == Blocks.DIRT && world.getLightFor(EnumSkyBlock.SKY, position.add(i2, j4, j3)) > 0) {
                                    Biome biome = world.getBiome(blockpos);

                                    if (biome.topBlock.getBlock() == Blocks.MYCELIUM) {
                                        world.setBlockState(blockpos, Blocks.MYCELIUM.getDefaultState(), 2);
                                    } else {
                                        world.setBlockState(blockpos, Blocks.GRASS.getDefaultState(), 2);

                                        if (rand.nextInt(3) == 0) {
                                            WorldGeneratorWisdomTree tree = new WorldGeneratorWisdomTree(false);
                                            tree.generate(world, rand, blockpos);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
