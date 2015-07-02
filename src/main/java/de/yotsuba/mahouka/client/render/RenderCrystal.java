package de.yotsuba.mahouka.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderCrystal extends Render
{

    // protected ResourceLocation texture;

    protected float r = 1;

    protected float g = 1;

    protected float b = 1;

    protected float a = 1;

    public RenderCrystal(float r, float g, float b, float a)
    {
        // texture = new ResourceLocation(MahoukaMod.MODID + ":textures/entity/serpents/python.png");
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null; // texture;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float float1, float rpt)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glTranslated(x, y, z);
        GL11.glScalef(0.2f, 0.4f, 0.2f);
        GL11.glRotatef(-entity.rotationYaw - 90, 0, 1, 0);
        GL11.glRotatef(-entity.rotationPitch - 90, 0, 0, 1);
        GL11.glRotatef(System.currentTimeMillis() % 360, 0, 1, 0);

        GL11.glColor4f(r, g, b, a);

        int b = entity.getBrightnessForRender(rpt);
        int bx = b % 65536;
        int by = b / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, bx / 1.0F, by / 1.0F);

        Tessellator.instance.startDrawing(GL11.GL_TRIANGLE_FAN);

        Tessellator.instance.setNormal(-1f, 1f, -1f);
        Tessellator.instance.addVertex(0, 1, 0);
        Tessellator.instance.addVertex(+1, 0, +1);
        Tessellator.instance.addVertex(+1, 0, -1);

        Tessellator.instance.setNormal(+1f, 1f, +1f);
        Tessellator.instance.addVertex(-1, 0, -1);

        Tessellator.instance.setNormal(+1f, 1f, -1f);
        Tessellator.instance.addVertex(-1, 0, +1);

        Tessellator.instance.setNormal(-1f, 1f, -1f);
        Tessellator.instance.addVertex(+1, 0, +1);
        Tessellator.instance.draw();

        Tessellator.instance.startDrawing(GL11.GL_TRIANGLE_FAN);;

        Tessellator.instance.setNormal(-1f, -1f, -1f);
        Tessellator.instance.addVertex(0, -1, 0);
        Tessellator.instance.addVertex(+1, 0, +1);
        Tessellator.instance.addVertex(+1, 0, -1);

        Tessellator.instance.setNormal(+1f, -1f, +1f);
        Tessellator.instance.addVertex(-1, 0, -1);

        Tessellator.instance.setNormal(+1f, -1f, -1f);
        Tessellator.instance.addVertex(-1, 0, +1);

        Tessellator.instance.setNormal(-1f, -1f, -1f);
        Tessellator.instance.addVertex(+1, 0, +1);
        Tessellator.instance.draw();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}
