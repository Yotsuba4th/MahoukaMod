package de.yotsuba.mahouka.client.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class EffectChanneling extends Effect
{

    protected EntityPlayer player;

    public EffectChanneling(EntityPlayer player, double x, double y, double z)
    {
        super(x, y, z);
        this.player = player;
    }

    @Override
    public void update()
    {
        roll += 6.0f;
        // age = 0;
        if (++age > maxAge)
            setDead();
    }

    @Override
    public void render(float partialTickTime)
    {
        if (player == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
            return;

        double xt = (player.prevPosX + (player.posX - player.prevPosX) * partialTickTime);
        double yt = (player.prevPosY + (player.posY - player.prevPosY) * partialTickTime);
        double zt = (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTickTime);

        scale = 0.4f;

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL11.glPushMatrix();
        GL11.glTranslated(xt, yt, zt);

        GL11.glRotatef(180 - player.renderYawOffset, 0, 1, 0);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        float f5 = 0.0625F;
        GL11.glTranslatef(-0.5f, -0.240F * f5 - 0.0078125F, 0.0F);
        float f6 = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTickTime;
        float f7 = player.limbSwing - player.limbSwingAmount * (1.0F - partialTickTime);
        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }
        float armAngle = MathHelper.cos(f7 * 0.6662F + (float) Math.PI) * 2.0F * f6 * 0.5F;
        GL11.glTranslatef(0, 0.2f, 0);
        GL11.glRotatef(armAngle * 30 + 75, 1, 0, 0);
        GL11.glTranslated(0, -0.4, -1.1);
        GL11.glRotatef(-10, 1, 0, 0);

        GL11.glRotatef(roll, 0, 0, 1);

        GL11.glScalef(scale, scale, scale);

        // GL11.glRotatef(-yaw, 0, 1, 0);
        // GL11.glRotatef(pitch, 1, 0, 0);
        // GL11.glRotatef(roll, 0, 0, 1);
        // GL11.glScalef(scale, scale, scale);

        // Handle color and alpha fading
        float alpha = a;
        if (fadeIn > 0 && age < fadeIn)
            alpha = alpha * age / fadeIn;
        else if (fadeOut > 0 && age > maxAge - fadeOut)
            alpha = alpha * (maxAge - age) / fadeOut;
        GL11.glColor4f(r, g, b, alpha);

        // Handle texture
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        float u1 = icon.getMinU();
        float u2 = icon.getMaxU();
        float v1 = icon.getMinV();
        float v2 = icon.getMaxV();

        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(-1, -1, 0, u2, v2);
        tes.addVertexWithUV(-1, +1, 0, u2, v1);
        tes.addVertexWithUV(+1, +1, 0, u1, v1);
        tes.addVertexWithUV(+1, -1, 0, u1, v2);
        tes.draw();

        GL11.glPopMatrix();
    }

}
