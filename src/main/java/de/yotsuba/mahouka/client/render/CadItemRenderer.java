package de.yotsuba.mahouka.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class CadItemRenderer implements IItemRenderer
{

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type)
        {
        case EQUIPPED:
        case EQUIPPED_FIRST_PERSON:
            return false;
        case ENTITY:
        case INVENTORY:
        case FIRST_PERSON_MAP:
        default:
            return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        switch (helper)
        {
        case ENTITY_ROTATION:
        case ENTITY_BOBBING:
        case BLOCK_3D:
        case EQUIPPED_BLOCK:
        case INVENTORY_BLOCK:
        default:
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        // GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.7f, 0.7f, 0);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        IIcon icon = item.getIconIndex();
        float u1 = icon.getMinU();
        float u2 = icon.getMaxU();
        float v1 = icon.getMinV();
        float v2 = icon.getMaxV();
        
        GL11.glColor4f(1, 1, 1, 1);

        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(+1, +1, 0, u1, v1);
        tes.addVertexWithUV(-1, +1, 0, u2, v1);
        tes.addVertexWithUV(-1, -1, 0, u2, v2);
        tes.addVertexWithUV(+1, -1, 0, u1, v2);
        tes.draw();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}
