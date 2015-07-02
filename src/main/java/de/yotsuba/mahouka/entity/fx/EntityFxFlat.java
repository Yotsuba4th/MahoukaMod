package de.yotsuba.mahouka.entity.fx;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityFxFlat extends EntityFxExt
{

    public EntityFxFlat(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityFxFlat(World world, double x, double y, double zz, double vx, double vy, double vz)
    {
        super(world, x, y, zz, vx, vy, vz);
    }

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
        tes.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        tes.addVertexWithUV(f11 - particleScale, f12, f13 - particleScale, u2, v2);
        tes.addVertexWithUV(f11 - particleScale, f12, f13 + particleScale, u2, v1);
        tes.addVertexWithUV(f11 + particleScale, f12, f13 + particleScale, u1, v1);
        tes.addVertexWithUV(f11 + particleScale, f12, f13 - particleScale, u1, v2);
    }

}