package de.yotsuba.mahouka.entity.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class EntityCastBreakingFX extends EntityFX
{

    private static final float PART_SIZE = 0.3f;

    public EntityCastBreakingFX(World world, double x, double y, double z)
    {
        super(world, x, y, z, 0, 0, 0);
        // particleGravity = Blocks.snow.blockParticleGravity;
        motionY -= 1f / 10f;
        motionX *= 0.1f;
        motionY *= 0.1f;
        motionZ *= 0.1f;
        particleMaxAge = 20 + rand.nextInt(20);
        particleTextureJitterX = rand.nextFloat() * (1f / PART_SIZE - 1);
        particleTextureJitterY = rand.nextFloat() * (1f / PART_SIZE - 1);
    }

    public void setColor(float r, float g, float b, float a)
    {
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleAlpha = a;
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @Override
    public void setParticleIcon(IIcon p_110125_1_)
    {
        particleIcon = p_110125_1_;
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_,
            float p_70539_7_)
    {
        float u1 = (particleTextureIndexX + particleTextureJitterX / 4) / 16;
        float u2 = u1 + 0.015609375F;
        float v1 = (particleTextureIndexY + particleTextureJitterY / 4) / 16;
        float v2 = v1 + 0.015609375F;
        float size = 0.1f * particleScale;

        if (particleIcon != null)
        {
            u1 = particleIcon.getInterpolatedU(particleTextureJitterX * PART_SIZE * 16);
            u2 = particleIcon.getInterpolatedU((particleTextureJitterX + 1) * PART_SIZE * 16);
            v1 = particleIcon.getInterpolatedV(particleTextureJitterY * PART_SIZE * 16);
            v2 = particleIcon.getInterpolatedV((particleTextureJitterY + 1) * PART_SIZE * 16);
        }

        float x = (float) (prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
        float y = (float) (prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
        float z = (float) (prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
        p_70539_1_.setColorOpaque_F(particleRed, particleGreen, particleBlue);
        p_70539_1_.addVertexWithUV(x - p_70539_3_ * size - p_70539_6_ * size, y - p_70539_4_ * size, z - p_70539_5_ * size - p_70539_7_ * size, u1, v2);
        p_70539_1_.addVertexWithUV(x - p_70539_3_ * size + p_70539_6_ * size, y + p_70539_4_ * size, z - p_70539_5_ * size + p_70539_7_ * size, u1, v1);
        p_70539_1_.addVertexWithUV(x + p_70539_3_ * size + p_70539_6_ * size, y + p_70539_4_ * size, z + p_70539_5_ * size + p_70539_7_ * size, u2, v1);
        p_70539_1_.addVertexWithUV(x + p_70539_3_ * size - p_70539_6_ * size, y - p_70539_4_ * size, z + p_70539_5_ * size - p_70539_7_ * size, u2, v2);
    }

}
