package projectat.trutils.core.tile;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public interface IGuiProvider {

    GuiContainer getClientGui(EntityPlayer player);

    Container getServerGui(EntityPlayer player);

}
