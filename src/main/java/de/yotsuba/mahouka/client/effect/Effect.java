package de.yotsuba.mahouka.client.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.util.WorldUtils;

@SideOnly(Side.CLIENT)
public class Effect
{

    private boolean isDead;

    public int age;

    public int maxAge;

    protected String tag;

    protected IIcon icon;

    public double x;
    public double y;
    public double z;

    protected double lastX;
    protected double lastY;
    protected double lastZ;

    public float yaw;
    public float pitch;
    public float roll;

    public float vYaw;
    public float vPitch;
    public float vRoll;

    public float vx;
    public float vy;
    public float vz;

    protected float r;
    protected float g;
    protected float b;
    protected float a;

    public int fadeIn;
    public int fadeOut;

    public float scale;

    public Effect(double x, double y, double z)
    {
        this(x, y, z, 0, 0, 0);
    }

    public Effect(double x, double y, double z, float vx, float vy, float vz)
    {
        this(x, y, z, vx, vy, vz, 1, 1, 1, 1);
    }

    public Effect(double x, double y, double z, float vx, float vy, float vz, float r, float g, float b, float a)
    {
        setPosition(x, y, z);
        setVelocity(vx, vy, vz);
        setColor(r, g, b, a);
        setMaxAge(Integer.MAX_VALUE);
        setScale(1);
    }

    public void setColor(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setVelocity(float vx, float vy, float vz)
    {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public void setRotation(float yaw, float pitch, float roll)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public void setPosition(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPositionOnGround(World world, double x, double y, double z)
    {
        setPosition(x, WorldUtils.dropOnGround(world, x, y, z) + 0.501f, z);
    }

    public void setDead()
    {
        isDead = true;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public void lookAt(Vec3 lookAt)
    {
        double xd = lookAt.xCoord - x;
        double yd = lookAt.yCoord - y;
        double zd = lookAt.zCoord - z;
        yaw = (float) (Math.atan2(zd, xd) * 180 / Math.PI) - 90;
        pitch = (float) (Math.atan2(Math.sqrt(xd * xd + zd * zd), yd) * 180 / Math.PI) - 90;
    }

    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public void setIcon(IIcon icon)
    {
        this.icon = icon;
    }

    public void update()
    {
        lastX = x;
        lastY = y;
        lastZ = z;

        x += vx;
        y += vy;
        z += vz;

        yaw += vYaw;
        pitch += vPitch;
        roll += vRoll;

        if (++age > maxAge)
            setDead();
    }

    public void cancel()
    {
        /* do nothing */
        age = maxAge - fadeOut;
    }

    public void render(float partialTickTime)
    {
        double xt = (lastX + (x - lastX) * partialTickTime);
        double yt = (lastY + (y - lastY) * partialTickTime);
        double zt = (lastZ + (z - lastZ) * partialTickTime);

        GL11.glPushMatrix();
        GL11.glTranslated(xt, yt, zt);
        GL11.glRotatef(-yaw, 0, 1, 0);
        GL11.glRotatef(pitch, 1, 0, 0);
        GL11.glRotatef(roll, 0, 0, 1);
        GL11.glScalef(scale, scale, scale);

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

    public boolean isDead()
    {
        return isDead;
    }

    public boolean isSimilarTo(Effect fx)
    {
        if (tag != null && !tag.equals(fx.tag))
            return false;
        double dx = x - fx.x;
        double dy = y - fx.y;
        double dz = z - fx.z;
        if (dx * dx + dy * dy + dz * dz > 0.1)
            return false;

        return true;
    }

}