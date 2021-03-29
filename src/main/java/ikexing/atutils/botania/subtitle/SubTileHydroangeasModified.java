package ikexing.atutils.botania.subtitle;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;

public class SubTileHydroangeasModified extends SubTileGenerating {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final String TAG_COOLDOWN = "cooldown";

    int burnTime, cooldown;

    @Override
    public void onUpdate() {
        super.onUpdate();
        BlockPos pos = supertile.getPos();

        if(burnTime > 0){
            burnTime--;
        }

        if(burnTime == 0){
            if(mana < getMaxMana() && !getWorld().isRemote){
                List<BlockPos> offsets = new ArrayList<BlockPos>();
                boolean hasLava = false;

                for(BlockPos.MutableBlockPos mb : BlockPos.getAllInBoxMutable(
                        pos.add(-2, 0, -2),
                        pos.add(2, 0, 2))){

                    if(getWorld().getBlockState(mb).getMaterial() == Material.LAVA){
                        hasLava = true;
                    }

                    int fluidAround = 0;
                    for (EnumFacing dir : EnumFacing.HORIZONTALS){
                        if(supertile.getWorld().getBlockState(mb.offset(dir)).getMaterial() == Material.LAVA)
                            fluidAround++;
                        if(fluidAround > 2)
                            supertile.getWorld().setBlockToAir(mb);
                    }

                    if(cooldown == 0)
                        burnTime += 40;
                    else cooldown = 0;
                    sync();
                    playSound();

                    break;
                }
            }
        } else {
            if(supertile.getWorld().rand.nextInt(8) == 0)
                doBurnParticles();
            burnTime--;
            if(burnTime == 0) {
                cooldown = 0;
                sync();
            }
        }

    }
    public void doBurnParticles() {
        Botania.proxy.wispFX(supertile.getPos().getX() + 0.55 + Math.random() * 0.2 - 0.1, supertile.getPos().getY() + 0.55 + Math.random() * 0.2 - 0.1, supertile.getPos().getZ() + 0.5, 0.05F, 0.05F, 0.7F, (float) Math.random() / 6, (float) -Math.random() / 60);
    }

    public void playSound() {
        getWorld().playSound(null, supertile.getPos(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.01F, 0.5F + (float) Math.random() * 0.5F);
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);

        cmp.setInteger(TAG_BURN_TIME, burnTime);
        cmp.setInteger(TAG_COOLDOWN, cooldown);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInteger(TAG_BURN_TIME);
        cooldown = cmp.getInteger(TAG_COOLDOWN);
    }

    @Override
    public void populateDropStackNBTs(List<ItemStack> drops) {
        super.populateDropStackNBTs(drops);
        int cooldown = this.cooldown;
        if(burnTime > 0)
            cooldown = 0;

        if(cooldown > 0)
            ItemNBTHelper.setInt(drops.get(0), TAG_COOLDOWN, 0);
    }

    @Override
    public LexiconEntry getEntry() {
        return LexiconData.hydroangeas;
    }

    @Override
    public int getMaxMana() {
        return 1500;
    }
}
