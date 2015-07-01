package de.yotsuba.mahouka.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.block.BlockProcessProgrammer;
import de.yotsuba.mahouka.gui.container.ProcessProgrammerContainer;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.magic.MagicProcess;

public class ProcessProgrammerGui extends GuiContainerExt
{

    public static final int GUIID = 0;

    private final ProcessProgrammerContainer container;

    private MagicProcess process;

    // TODO: Design gui texture
    private static final ResourceLocation texture = new ResourceLocation(MahoukaMod.MODID + ":textures/gui/" + BlockProcessProgrammer.ID + ".png");

    public ProcessProgrammerGui(InventoryPlayer playerInventory)
    {
        super(new ProcessProgrammerContainer(playerInventory));
        container = (ProcessProgrammerContainer) inventorySlots;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

        String header = BlockProcessProgrammer.BLOCK.getLocalizedName();
        fontRendererObj.drawString(header, (xSize - fontRendererObj.getStringWidth(header)) / 2 + 10, 6, 4210752);

        if (process != null)
            process.drawGui(this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // Dynamically update gui
        if (container.needGuiUpdate)
        {
            container.needGuiUpdate = false;
            process = getProcess();
            buttonList.clear();
            if (process != null)
                process.updateGui(this);
            // buttonList.add(new GuiButtonExt(1, x, y, 8, 8, "+"));
        }
    }

    public MagicProcess getProcess()
    {
        ItemStack stack = container.getSlot(0).getStack();
        if (stack != null)
        {
            NBTTagCompound stackData = ((ItemMagicProcess) stack.getItem()).getStackData(stack);
            return MagicProcess.createFromNBT(stackData);
        }
        return null;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        // TODO Auto-generated method stub
        super.actionPerformed(button);
    }
}
