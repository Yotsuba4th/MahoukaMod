package de.yotsuba.mahouka.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.block.BlockProcessProgrammer;
import de.yotsuba.mahouka.gui.container.ProcessProgrammerContainer;

public class ProcessProgrammerGui extends GuiContainerExt
{

    public static final int GUIID = 0;

    private final ProcessProgrammerContainer container;

    private static final ResourceLocation texture = new ResourceLocation(MahoukaMod.MODID + ":textures/gui/" + BlockProcessProgrammer.ID + ".png");

    /* ------------------------------------------------------------ */

    public ProcessProgrammerGui(InventoryPlayer playerInventory)
    {
        super(new ProcessProgrammerContainer(playerInventory));
        container = (ProcessProgrammerContainer) inventorySlots;
    }

    /* ------------------------------------------------------------ */

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(texture);

        String header = BlockProcessProgrammer.BLOCK.getLocalizedName();
        fontRendererObj.drawString(header, (xSize - fontRendererObj.getStringWidth(header)) / 2 + 10, 6, 4210752);

        // Dynamically update gui
        if (container.getProcess() != null)
            container.getProcess().guiDraw(this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(texture);
        drawTexturedModalRect(getX(), getY(), 0, 0, getWidth(), getHeight());

        // Dynamically update gui
        if (container.needGuiUpdate)
        {
            container.needGuiUpdate = false;
            buttonList.clear();
            if (container.getProcess() != null)
                container.getProcess().guiUpdate(this);
        }
    }

}
