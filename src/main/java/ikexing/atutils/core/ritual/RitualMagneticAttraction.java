package ikexing.atutils.core.ritual;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import ikexing.atutils.ATUtils;
import ikexing.atutils.core.ritual.entity.EntityRitualMagneticAttraction;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class RitualMagneticAttraction extends RitualBase {

    public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(2400);
    public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 16).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
    public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 16).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
    public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 16).setDescription("Radius on the X Axis of the cube in which the ritual takes place");

    public double radius_x, radius_y, radius_z;

    public RitualMagneticAttraction() {
        super("magnetic_attraction", false);
        properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
        setEntityClass(EntityRitualMagneticAttraction.class);
    }

    @Override
    public void init() {
        recipe = new RitualRecipe(this,
                new ItemStack(ModItems.runic_dust),
                new ItemStack(ModItems.pereskia),
                new ItemStack(ModItems.stalicripe),
                new ItemStack(Items.FLINT),
                new ItemStack(Items.COAL, 1, 1)
        );
        setIcon(ATUtils.magneticAttraction);
        setColor(TextFormatting.WHITE);
        addCondition(new ConditionStandingStones(3, 1));
    }

    @Override
    public void doFinalise() {
        duration = properties.get(PROP_DURATION);
        int[] radius = properties.getRadius();
        radius_x = radius[0];
        radius_y = radius[1];
        radius_z = radius[2];
    }
}
