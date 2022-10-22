package projectat.trutils.core.events;

import com.google.common.collect.Sets;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.items.ModItems;
import net.minecraft.util.EnumFacing;
import projectat.trutils.Main;
import projectat.trutils.core.block.BlockEvilStone;
import projectat.trutils.core.fluids.FluidHandlerAuraBottle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.psi.api.cad.RegenPsiEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber
public class EventHandler {

    public static final Map<EntityPlayer, Boolean> IS_FIRST = new HashMap<>();

    public static void syncAroundEvilStone(BlockPos pos, World world, IBlockState state, Set<BlockPos> evilStoneBlockPos) {
        if (!evilStoneBlockPos.contains(pos)) {
            evilStoneBlockPos.add(pos);
            if (state.getValue(BlockEvilStone.STATUS) < 5) {
                Integer status = state.getValue(BlockEvilStone.STATUS);
                if (!world.isRemote) {
                    world.setBlockState(pos, state.withProperty(BlockEvilStone.STATUS, status + 1));
                } else {
                    wispFx(pos);
                }
            }
        }
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos offset = pos.offset(facing);
            if (evilStoneBlockPos.contains(offset))
                continue;
            if (world.getBlockState(offset).getBlock() instanceof BlockEvilStone)
                syncAroundEvilStone(offset, world, world.getBlockState(offset), evilStoneBlockPos);
        }
    }

    private static void wispFx(BlockPos pos) {
        for (int i = 0; i < 5; i++) {
            Botania.proxy.wispFX(pos.getX() + Math.random() * 1.55D, pos.getY() * Math.random() * 2.0D, pos.getZ() + Math.random() * 1.55D, 0.5F, 0.0F, 0.0F, (float) Math.random() * 2.35F);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();


        if (entity instanceof EntityCreature || entity instanceof EntitySlime) {
            if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;

            BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            Iterable<BlockPos> allInBox = BlockPos.getAllInBox(pos.add(4, 5, 4), pos.add(-4, -5, -4));
            for (BlockPos blockPos : allInBox) {
                IBlockState blockState = world.getBlockState(blockPos);
                if (world.getBlockState(blockPos).getBlock() instanceof BlockEvilStone) {
                    if (world.rand.nextBoolean())
                        syncAroundEvilStone(blockPos, world, blockState, Sets.newHashSet());
                    // 只要找到一次就算找到 1/2几率
                    break;
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onRegenPsi(RegenPsiEvent event) {
        EntityPlayer player = event.getPlayer();
        World world = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        int consume = event.getRegenRate();
        if (world.isRemote) return;
        if (event.getPlayerPsi() != event.getPlayerPsiCapacity()) {
            if (!ManaItemHandler.requestManaExact(event.getCad(), player, consume * 5, true)) {
                BlockPos highestSpot = IAuraChunk.getHighestSpot(world, pos, 4, pos);
                BlockPos lowestSpot = IAuraChunk.getLowestSpot(world, pos, 4, pos);
                if (IAuraChunk.getAuraInArea(world, lowestSpot, 4) < 500000) {
                    if (!IS_FIRST.containsKey(player)) {
                        IS_FIRST.put(player, true);
                        player.sendMessage(new TextComponentString(I18n.translateToLocal("trutils.data.content")));
                    }
                    event.setCanceled(true);
                    return;
                }
                IAuraChunk.getAuraChunk(world, highestSpot).drainAura(highestSpot, consume * 10);
                IAuraChunk.getAuraChunk(world, highestSpot).markDirty();
                if (IS_FIRST.containsKey(player)) {
                    IS_FIRST.remove(player, true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().isEmpty()) {
            if (event.getItemStack().getItem() == Main.equivalentFuel) event.setBurnTime(200);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == ModItems.AURA_BOTTLE || event.getObject().getItem() == ModItems.BOTTLE_TWO) {
            event.addCapability(new ResourceLocation(Main.MODID, "aura_bottle"), new FluidHandlerAuraBottle(event.getObject()));
        }
    }

}
