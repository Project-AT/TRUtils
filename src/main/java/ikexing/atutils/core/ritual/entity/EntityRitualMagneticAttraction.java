package ikexing.atutils.core.ritual.entity;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.ritual.RitualMagneticAttraction;
import net.minecraft.world.World;

public class EntityRitualMagneticAttraction extends EntityRitualBase {

    private RitualMagneticAttraction ritual;

    public EntityRitualMagneticAttraction(World worldIn) {
        super(worldIn);
        getDataManager().register(lifetime, ATUtils.ritualMa.getDuration() + 20);
        ritual = (RitualMagneticAttraction) ATUtils.ritualMa;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        System.out.println(1);
    }
}
