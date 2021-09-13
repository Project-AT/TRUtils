package ikexing.atutils.core.block;

import ikexing.atutils.ATUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockRustyIron extends Block {

    public static final String NAME = "rusty_iron";

    public static final Block INSTANCE = new BlockRustyIron();
    public static final Item ITEM_BLOCK = new ItemBlock(INSTANCE).setRegistryName(NAME);

    public BlockRustyIron() {
        super(Material.IRON);
        setHardness(6.0F);
        setResistance(12.0F);
        setTranslationKey(ATUtils.MODID + "." + NAME);
        setRegistryName(NAME);
    }

}
