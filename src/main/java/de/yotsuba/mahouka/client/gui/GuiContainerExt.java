package de.yotsuba.mahouka.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.gui.ButtenClickListener;
import de.yotsuba.mahouka.network.S6ButtonClick;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerExt extends GuiContainer
{
    
    public GuiContainerExt(Container container)
    {
        super(container);
    }

    /* ------------------------------------------------------------ */

    public int getX()
    {
        return (width - xSize) / 2;
    }

    public int getY()
    {
        return (height - ySize) / 2;
    }

    public int getWidth()
    {
        return xSize;
    }

    public int getHeight()
    {
        return ySize;
    }

    /* ------------------------------------------------------------ */

    @SuppressWarnings("unchecked")
    public List<GuiButton> getButtons()
    {
        return buttonList;
    }

    @SuppressWarnings("unchecked")
    public List<GuiLabel> getLabels()
    {
        return labelList;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (inventorySlots instanceof ButtenClickListener)
            ((ButtenClickListener) inventorySlots).buttonClicked(button.id);
        S6ButtonClick.send(button.id);
    }

    /* ------------------------------------------------------------ */

    public TextureManager getRenderEngine()
    {
        return Minecraft.getMinecraft().renderEngine;
    }

    public void bindTexture(ResourceLocation texture)
    {
        getRenderEngine().bindTexture(texture);
    }

    public FontRenderer getFontRenderer()
    {
        return fontRendererObj;
    }

    public static int getColor(int r, int g, int b, int a)
    {
        return (a & 255) << 24 | (r & 255) << 16 | (b & 255) << 8 | (g & 255);
    }

}
