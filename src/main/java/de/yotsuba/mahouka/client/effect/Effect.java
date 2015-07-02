package de.yotsuba.mahouka.client.effect;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import de.yotsuba.mahouka.util.WorldUtils;

// TODO: Probably need to write our own particle renderer to handle these with special cases
// Current problem is that it is very complicated to calculate rotations for the texture which would be way easier if we  can directly use OpenGL and its matrices
// This will also allow us to use large textures for effects as well
public class Effect
{

    private boolean isDead;

    public int maxAge;

    protected IIcon icon;

    protected double x;
    protected double y;
    protected double z;

    protected double xt;
    protected double yt;
    protected double zt;

    protected double lastX;
    protected double lastY;
    protected double lastZ;

    protected float yaw;
    protected float pitch;
    protected float roll;

    protected float vx;
    protected float vy;
    protected float vz;

    protected float r;
    protected float g;
    protected float b;
    protected float a;

    protected float scale;

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
        setMaxAge(20);
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

    public void setPosition(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPositionOnGround(World world, double x, double y, double z)
    {
        setPosition(x, WorldUtils.dropOnGround(world, x, y, z) + 0.51f, z);
    }

    public void lookAt(Vec3 lookAt)
    {
        double xd = lookAt.xCoord - x;
        double yd = lookAt.yCoord - y;
        double zd = lookAt.zCoord - z;
        yaw = (float) (Math.atan2(zd, xd) * 180 / Math.PI) - 90;
        pitch = (float) (Math.atan2(Math.sqrt(xd * xd + zd * zd), yd) * 180 / Math.PI) - 90;
        roll = 0;
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

    public void renderParticle(float partialTickTime)
    {
        float rX = ActiveRenderInfo.rotationX;
        float rZ = ActiveRenderInfo.rotationZ;
        float rYZ = ActiveRenderInfo.rotationYZ;
        float rXY = ActiveRenderInfo.rotationXY;
        float rXZ = ActiveRenderInfo.rotationXZ;

        float u1 = icon.getMinU();
        float u2 = icon.getMaxU();
        float v1 = icon.getMinV();
        float v2 = icon.getMaxV();

        updatePartialPosition(partialTickTime);

        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();

        tes.setColorRGBA_F(r, g, b, a);
        tes.addVertexWithUV(xt - rX * scale - rYZ * scale, yt - rXZ * scale, zt - rZ * scale - rXY * scale, u2, v2);
        tes.addVertexWithUV(xt - rX * scale + rYZ * scale, yt + rXZ * scale, zt - rZ * scale + rXY * scale, u2, v1);
        tes.addVertexWithUV(xt + rX * scale + rYZ * scale, yt + rXZ * scale, zt + rZ * scale + rXY * scale, u1, v1);
        tes.addVertexWithUV(xt + rX * scale - rYZ * scale, yt - rXZ * scale, zt + rZ * scale - rXY * scale, u1, v2);

        tes.draw();
    }

    protected void updatePartialPosition(float partialTickTime)
    {
        xt = (lastX + (x - lastX) * partialTickTime);
        yt = (lastY + (y - lastY) * partialTickTime);
        zt = (lastZ + (z - lastZ) * partialTickTime);
    }

    public boolean isDead()
    {
        return isDead;
    }

    public void update()
    {
        lastX = x;
        lastY = y;
        lastZ = z;

        x += vx;
        y += vy;
        z += vz;
    }

}