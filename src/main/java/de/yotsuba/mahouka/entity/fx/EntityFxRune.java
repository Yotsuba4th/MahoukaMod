package de.yotsuba.mahouka.entity.fx;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import de.yotsuba.mahouka.MahoukaMod;

public class EntityFxRune extends EntityFX
{

    private boolean flat;

    protected EntityFxRune(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityFxRune(World world, double x, double y, double z, double vx, double vy, double vz)
    {
        super(world, x, y, z, vx, vy, vz);
        motionX = motionY = motionZ = 0;
        particleMaxAge = 60;
        setParticleIcon(MahoukaMod.icon_rune_default);
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

    public void setFlat(boolean flat)
    {
        this.flat = flat;
        posY = dropOnBlock(posX, posY, posZ);
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        super.setPosition(x, y, z);
        // setPositionDropped(x, y, z);
    }

    public void setPositionDropped(double x, double y, double z)
    {
        super.setPosition(x, dropOnBlock(x, y, z) + 0.51, z);
    }

    public double dropOnBlock(double posX, double posY, double posZ)
    {
        if (flat)
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
        }
        return posY;
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    // rpt = mc.timer.renderPartialTicks
    @Override
    public void renderParticle(Tessellator tes, float rpt, float cosYaw, float cosPitch, float sinYaw, float sinPitch, float sinPitch2)
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

        // Adjust positioning and size
        float s = 1.0f * particleScale;

        tes.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        if (flat)
        {
            tes.addVertexWithUV(f11 - s, f12, f13 - s, u2, v2);
            tes.addVertexWithUV(f11 - s, f12, f13 + s, u2, v1);
            tes.addVertexWithUV(f11 + s, f12, f13 + s, u1, v1);
            tes.addVertexWithUV(f11 + s, f12, f13 - s, u1, v2);
        }
        else
        {
            tes.addVertexWithUV(f11 - cosYaw * s - sinPitch * s, f12 - cosPitch * s, f13 - sinYaw * s - sinPitch2 * s, u2, v2);
            tes.addVertexWithUV(f11 - cosYaw * s + sinPitch * s, f12 + cosPitch * s, f13 - sinYaw * s + sinPitch2 * s, u2, v1);
            tes.addVertexWithUV(f11 + cosYaw * s + sinPitch * s, f12 + cosPitch * s, f13 + sinYaw * s + sinPitch2 * s, u1, v1);
            tes.addVertexWithUV(f11 + cosYaw * s - sinPitch * s, f12 - cosPitch * s, f13 + sinYaw * s - sinPitch2 * s, u1, v2);
        }
    }

}
