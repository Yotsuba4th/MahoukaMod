package de.yotsuba.mahouka.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.block.BlockSequenceProgrammer;
import de.yotsuba.mahouka.gui.container.SequenceProgrammerContainer;

public class SequenceProgrammerGui extends GuiContainerExt
{

    public static final int GUIID = 1;

    private static final ResourceLocation texture = new ResourceLocation(MahoukaMod.MODID + ":textures/gui/" + BlockSequenceProgrammer.ID + ".png");

    private final SequenceProgrammerContainer container;

    public SequenceProgrammerGui(InventoryPlayer playerInventory)
    {
        super(new SequenceProgrammerContainer(playerInventory));
        container = (SequenceProgrammerContainer) inventorySlots;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

        String header = BlockSequenceProgrammer.BLOCK.getLocalizedName();
        fontRendererObj.drawString(header, (xSize - fontRendererObj.getStringWidth(header)) / 2, 6, 4210752);

        String text = "";
        switch (container.state)
        {
        case COMPOSING:
            text = "> Composing";
            break;
        case DECOMPOSE_PENDING:
            text = "< Decomposing +";
            break;
        case DECOMPOSING:
            text = "< Decomposing";
            break;
        case ERROR:
            text = "x ERROR";
            break;
        case IDLE:
            text = "- Idle";
            break;
        }
        fontRendererObj.drawString(text, xSize / 2, 65, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

}
