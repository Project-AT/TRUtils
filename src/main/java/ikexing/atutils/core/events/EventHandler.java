package ikexing.atutils.core.events;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.advancement.VisitVillageTrigger;
import ikexing.atutils.core.block.BlockEvilStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.psi.api.cad.RegenPsiEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class EventHandler {

    public static final Map<EntityPlayer, Boolean> IS_FIRST = new HashMap<>();

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();

        if (entity instanceof EntityCreature || entity instanceof EntitySlime) {
            BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            Iterable<BlockPos> allInBox = BlockPos.getAllInBox(pos.add(4, 5, 4), pos.add(-4, -5, -4));
            for (BlockPos blockPos : allInBox) {
                IBlockState blockState = world.getBlockState(blockPos);
                if (world.getBlockState(blockPos).getBlock() instanceof BlockEvilStone) {
                    Integer status = blockState.getValue(BlockEvilStone.STATUS);
                    if (blockState.getValue(BlockEvilStone.STATUS) < 5) {
                        if (!world.isRemote) {
                            world.setBlockState(blockPos, blockState.withProperty(BlockEvilStone.STATUS, status + 1));
                        } else {
                            for (int i = 0; i < 5; i++) {
                                Botania.proxy.wispFX(blockPos.getX() + Math.random() * 1.55D, blockPos.getY() * Math.random() * 2.0D, blockPos.getZ() + Math.random() * 1.55D, 0.5F, 0.0F, 0.0F, (float) Math.random() * 2.35F);
                            }
                        }

                        break;
                    }
                }
            }
        }
    }

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
                        player.sendMessage(new TextComponentString(I18n.translateToLocal("atutils.data.content")));
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
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side.isServer()) {
            VisitVillageTrigger.INSTANCE.trigger(((EntityPlayerMP) event.player));
        }
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().isEmpty()) {
            if (event.getItemStack().getItem() == ATUtils.equivalentFuel) event.setBurnTime(200);
        }
    }

}
