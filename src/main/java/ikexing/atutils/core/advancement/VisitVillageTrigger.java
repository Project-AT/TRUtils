package ikexing.atutils.core.advancement;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import ikexing.atutils.ATUtils;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author youyihj
 */
public enum VisitVillageTrigger implements ICriterionTrigger<VisitVillageTrigger.Instance> {
    INSTANCE;

    private static final ResourceLocation ID = new ResourceLocation(ATUtils.MODID, "visit_village");
    private final Map<PlayerAdvancements, Set<Listener<Instance>>> listeners = new HashMap<>();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancementsIn, it -> new HashSet<>()).add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
        Set<Listener<VisitVillageTrigger.Instance>> listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return Instance.INSTANCE;
    }

    public void trigger(EntityPlayerMP player) {
        PlayerAdvancements advancements = player.getAdvancements();
        Set<Listener<Instance>> listeners = this.listeners.get(advancements);
        if (listeners != null) {
            List<Listener<Instance>> triggered = new ArrayList<>();
            for (Listener<VisitVillageTrigger.Instance> listener : listeners) {
                if (listener.getCriterionInstance().test(player)) {
                    triggered.add(listener);
                }
            }
            for (Listener<VisitVillageTrigger.Instance> listener : triggered) {
                listener.grantCriterion(advancements);
            }
        }
    }

    public enum Instance implements ICriterionInstance, Predicate<EntityPlayerMP> {
        INSTANCE;

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public String toString() {
            return "VisitVillageCriterionInstance";
        }

        @Override
        public boolean test(EntityPlayerMP player) {
            return player.world.getVillageCollection().getVillageList().stream().anyMatch(village -> village.isBlockPosWithinSqVillageRadius(player.getPosition()));
        }
    }
}
