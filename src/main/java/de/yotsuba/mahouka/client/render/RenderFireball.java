package de.yotsuba.mahouka.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFireball extends Render
{
    private float size;

    public RenderFireball(float size)
    {
        this.size = size;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationItemsTexture;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(size / 1.0F, size / 1.0F, size / 1.0F);
        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        bindEntityTexture(entity);
        IIcon icon = Items.fire_charge.getIconFromDamage(0);
        float u1 = icon.getMinU();
        float u2 = icon.getMaxU();
        float v1 = icon.getMinV();
        float v2 = icon.getMaxV();

        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 1.0F, 0.0F);
        tes.addVertexWithUV(0.0F - 0.5F, 0.0F - 0.25F, 0.0D, u1, v2);
        tes.addVertexWithUV(1.0F - 0.5F, 0.0F - 0.25F, 0.0D, u2, v2);
        tes.addVertexWithUV(1.0F - 0.5F, 1.0F - 0.25F, 0.0D, u2, v1);
        tes.addVertexWithUV(0.0F - 0.5F, 1.0F - 0.25F, 0.0D, u1, v1);
        tes.draw();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

}