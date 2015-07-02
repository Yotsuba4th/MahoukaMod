package de.yotsuba.mahouka.entity.fx;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

// TODO: Probably need to write our own particle renderer to handle these with special cases
// Current problem is that it is very complicated to calculate rotations for the texture which would be way easier if we  can directly use OpenGL and its matrices
// This will also allow us to use large textures for effects as well
public class EntityFxExt extends EntityFX
{

    public EntityFxExt(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityFxExt(World world, double x, double y, double z, double vx, double vy, double vz)
    {
        super(world, x, y, z, vx, vy, vz);
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        particleMaxAge = 20;
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    public void setMaxAge(int particleMaxAge)
    {
        this.particleMaxAge = particleMaxAge;
    }

    public void setRadius(float scale)
    {
        particleScale = scale;
    }

    public void setColor(float r, float g, float b)
    {
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
    }

    public void setPositionOnGround(double x, double y, double z)
    {
        super.setPosition(x, dropOnGround(x, y, z) + 0.51, z);
    }

    public double dropOnGround(double posX, double posY, double posZ)
    {
        int x = (int) Math.floor(posX);
        int y = (int) Math.floor(posY);
        int z = (int) Math.floor(posZ);
        while (y >= 0)
        {
            Block block = worldObj.getBlock(x, y, z);
            if (block.getMaterial().isSolid())
            {
                return y + 0.5;
            }
            y--;
        }
        return posY;
    }

    @Override
    public void renderParticle(Tessellator tes, float rpt, float rX, float rXZ, float rZ, float rYZ, float rXY)
    {
        float u1 = particleTextureIndexX / 16.0F;
        float u2 = u1 + 0.0624375F;
        float v1 = particleTextureIndexY / 16.0F;
        float v2 = v1 + 0.0624375F;
        if (particleIcon != null)
        {
            u1 = particleIcon.getMinU();
            u2 = particleIcon.getMaxU();
            v1 = particleIcon.getMinV();
            v2 = particleIcon.getMaxV();
        }

        float f11 = (float) (prevPosX + (posX - prevPosX) * rpt - interpPosX);
        float f12 = (float) (prevPosY + (posY - prevPosY) * rpt - interpPosY);
        float f13 = (float) (prevPosZ + (posZ - prevPosZ) * rpt - interpPosZ);
        tes.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        tes.addVertexWithUV(f11 - rX * particleScale - rYZ * particleScale, f12 - rXZ * particleScale, f13 - rZ * particleScale - rXY * particleScale, u2, v2);
        tes.addVertexWithUV(f11 - rX * particleScale + rYZ * particleScale, f12 + rXZ * particleScale, f13 - rZ * particleScale + rXY * particleScale, u2, v1);
        tes.addVertexWithUV(f11 + rX * particleScale + rYZ * particleScale, f12 + rXZ * particleScale, f13 + rZ * particleScale + rXY * particleScale, u1, v1);
        tes.addVertexWithUV(f11 + rX * particleScale - rYZ * particleScale, f12 - rXZ * particleScale, f13 + rZ * particleScale - rXY * particleScale, u1, v2);
    }

    public void renderParticle(float partialTickTime)
    {
    }

}