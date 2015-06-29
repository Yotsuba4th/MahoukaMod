package de.yotsuba.mahouka.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
        case 0:
            return new ProcessAssemblerContainer(player.inventory);
        case 1:
            return new SequenceProgrammerContainer(player.inventory);
        case 2:
            return new CadProgrammerContainer(player.inventory);
        default:
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
        case 0:
            return new ProcessAssemblerGui(player.inventory);
        case 1:
            return new SequenceProgrammerGui(player.inventory);
        case 2:
            return new CadProgrammerGui(player.inventory);
        default:
            return null;
        }
    }

}
