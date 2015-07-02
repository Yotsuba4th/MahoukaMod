package de.yotsuba.mahouka.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import de.yotsuba.mahouka.util.Utils;

public class EntityMagicProjectileIce extends EntityMagicProjectile
{

    public EntityMagicProjectileIce(World world)
    {
        super(world);
        setSize(0.5f, 0.5f);
        maxTicksToLive = 200;
    }

    public EntityMagicProjectileIce(World world, EntityLivingBase shooter, double x, double y, double z)
    {
        super(world, shooter, x, y, z);
        setSize(0.5f, 0.5f);
        maxTicksToLive = 200;
    }

    @Override
    public float getDamage()
    {
        return 1;
    }

    @Override
    public void onImpactEntity(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase player = (EntityLivingBase) entity;
            player.addPotionEffect(new PotionEffect(2, Utils.millisecondsToTicks(1500), 3));
        }
    }

    @Override
    public void onImpactBlock(int x, int y, int z)
    {
        // worldObj.setBlock(x, y, z, Blocks.flowing_water);
    }

}
