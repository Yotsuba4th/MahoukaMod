package de.yotsuba.mahouka.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityMagicProjectileFire extends EntityMagicProjectile
{

    public EntityMagicProjectileFire(World world)
    {
        super(world);
        accelerationY = -0.01;
        setSize(0.3125F, 0.3125F);
    }

    public EntityMagicProjectileFire(World world, EntityLivingBase shooter, double x, double y, double z)
    {
        super(world, shooter, x, y, z);
        accelerationY = -0.01;
        setSize(0.3125F, 0.3125F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        setFire(1);
        worldObj.spawnParticle("smoke", posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public float getDamage()
    {
        return 1.0f;
    }

    @Override
    public void onImpactEntity(Entity entity)
    {
        entity.setFire(4);
        // int x = (int) Math.floor(posX);
        // int y = (int) Math.floor(posY);
        // int z = (int) Math.floor(posZ);
        // onImpactBlock(x, y, z);
    }

    @Override
    public void onImpactBlock(int x, int y, int z)
    {
        System.out.println(String.format("Set fire %d,%d,%d", x, y, z));
        worldObj.setBlock(x, y, z, Blocks.fire);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float p_70070_1_)
    {
        return 15728880;
    }

}
