package ikexing.atutils.core.spell;

import epicsquid.roots.integration.crafttweaker.Herbs;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;

public class RootsSpells {

    public static void init() {
        SpellBase chrysopoeiaSpell = SpellRegistry.getSpell("spell_chrysopoeia");
        PropertyTable chrysopoeiaSpellPropertiesrops = chrysopoeiaSpell.getProperties();
        Property<SpellBase.SpellCost> infernalBulbProp = chrysopoeiaSpellPropertiesrops.get("cost_" + Herbs.infernal_bulb.getHerbName());
        chrysopoeiaSpellPropertiesrops.getProperties().remove(infernalBulbProp);
        Property<SpellBase.SpellCost> spiritHerbProp = chrysopoeiaSpellPropertiesrops.get("cost_" + Herbs.spirit_herb.getHerbName());
        SpellBase.SpellCost spiritHerbNewCost = new SpellBase.SpellCost(Herbs.spirit_herb.getHerbName(), 0.5);
        chrysopoeiaSpellPropertiesrops.set(spiritHerbProp, spiritHerbNewCost);
    }
}
