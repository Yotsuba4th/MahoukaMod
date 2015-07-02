package de.yotsuba.mahouka.entity.fx;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class EntityFxDirected extends EntityFxExt
{

    public EntityFxDirected(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityFxDirected(World world, double x, double y, double zz, double vx, double vy, double vz)
    {
        super(world, x, y, zz, vx, vy, vz);
    }
    
    @Override
    public void renderParticle(Tessellator tes, float rpt, float rX_, float rXZ_, float rZ_, float rYZ_, float rXY_)
    {
        // rotationYaw += 1;
        float rX = MathHelper.cos(rotationYaw * 0.017453292F);
        float rXZ = MathHelper.cos(rotationPitch * 0.017453292F);
        float rZ = MathHelper.sin(rotationYaw * 0.017453292F);
        float rYZ = -rZ * MathHelper.sin(rotationPitch * 0.017453292F);
        float rXY = rX * MathHelper.sin(rotationPitch * 0.017453292F);

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

        float x = (float) (prevPosX + (posX - prevPosX) * rpt - interpPosX);
        float y = (float) (prevPosY + (posY - prevPosY) * rpt - interpPosY);
        float z = (float) (prevPosZ + (posZ - prevPosZ) * rpt - interpPosZ);
        GL11.glDisable(GL11.GL_CULL_FACE);
        tes.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        tes.addVertexWithUV(x - rX * particleScale - rYZ * particleScale, y - rXZ * particleScale, z - rZ * particleScale - rXY * particleScale, u2, v2);
        tes.addVertexWithUV(x - rX * particleScale + rYZ * particleScale, y + rXZ * particleScale, z - rZ * particleScale + rXY * particleScale, u2, v1);
        tes.addVertexWithUV(x + rX * particleScale + rYZ * particleScale, y + rXZ * particleScale, z + rZ * particleScale + rXY * particleScale, u1, v1);
        tes.addVertexWithUV(x + rX * particleScale - rYZ * particleScale, y - rXZ * particleScale, z + rZ * particleScale - rXY * particleScale, u1, v2);

//        tes.addVertexWithUV(x - rX * particleScale + rYZ * particleScale, y + rXZ * particleScale, z - rZ * particleScale + rXY * particleScale, u2, v1);
//        tes.addVertexWithUV(x - rX * particleScale - rYZ * particleScale, y - rXZ * particleScale, z - rZ * particleScale - rXY * particleScale, u2, v2);
//        tes.addVertexWithUV(x + rX * particleScale - rYZ * particleScale, y - rXZ * particleScale, z + rZ * particleScale - rXY * particleScale, u1, v2);
//        tes.addVertexWithUV(x + rX * particleScale + rYZ * particleScale, y + rXZ * particleScale, z + rZ * particleScale + rXY * particleScale, u1, v1);
    }

}