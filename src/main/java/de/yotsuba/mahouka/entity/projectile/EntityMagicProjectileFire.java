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
        setSize(0.3125F, 0.3125F);
    }

    public EntityMagicProjectileFire(World world, EntityLivingBase shooter, double x, double y, double z)
    {
        super(world, shooter, x, y, z);
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
        return 1;
    }

    @Override
    public void onImpactEntity(Entity entity)
    {
        entity.setFire(1);
    }

    @Override
    public void onImpactBlock(int x, int y, int z)
    {
        worldObj.setBlock(x, y, z, Blocks.fire);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float p_70070_1_)
    {
        return 15728880;
    }

}
