package de.yotsuba.mahouka.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.world.World;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.Targeting;

//TODO: Add interface to let this play a sound on acceleration for example
public class EntityMagicFireball extends EntitySmallFireball implements Targeting
{

    protected Target target;

    public EntityMagicFireball(World world)
    {
        super(world);
    }

    public EntityMagicFireball(World world, double x, double y, double z, double xd, double yd, double zd)
    {
        super(world, x, y, z, xd, yd, zd);
        accelerationX = xd;
        accelerationY = yd;
        accelerationZ = zd;
    }

    public EntityMagicFireball(World world, EntityLivingBase shooter, double xd, double yd, double zd)
    {
        super(world, shooter, xd, yd, zd);
        accelerationX = xd;
        accelerationY = yd;
        accelerationZ = zd;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (ticksExisted > Utils.secondsToTicks(15))
            setDead();
    }

    @Override
    public Target getTarget()
    {
        return target;
    }

    public void setTarget(Target target)
    {
        this.target = target;
    }

}
