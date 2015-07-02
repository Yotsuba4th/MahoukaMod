package de.yotsuba.mahouka.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.Targeting;

//TODO: Add interface to let this play a sound on acceleration for example
public abstract class EntityMagicProjectile extends Entity implements Targeting
{

    protected EntityLivingBase shootingEntity;

    protected int ticksAlive;

    protected double accelerationX;

    protected double accelerationY;

    protected double accelerationZ;

    protected Target target;

    public int maxTicksToLive = 250;

    /* ------------------------------------------------------------ */

    public EntityMagicProjectile(World world)
    {
        super(world);
        ySize = 0.5f;
        setSize(1.0F, 1.0F);
    }

    public EntityMagicProjectile(World world, EntityLivingBase shooter, double x, double y, double z)
    {
        this(world);
        setLocationAndAngles(x, y, z, 0, 0);
    }

    @Override
    protected void entityInit()
    {
    }

    /* ------------------------------------------------------------ */

    @Override
    public Target getTarget()
    {
        return target;
    }

    @Override
    public void setTarget(Target target)
    {
        this.target = target;
    }

    /* ------------------------------------------------------------ */

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("direction", 9))
        {
            NBTTagList tagList = tag.getTagList("direction", 6);
            this.motionX = tagList.func_150309_d(0);
            this.motionY = tagList.func_150309_d(1);
            this.motionZ = tagList.func_150309_d(2);
        }
        else
        {
            this.setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setTag("direction", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
    }

    /* ------------------------------------------------------------ */

    @Override
    public void onUpdate()
    {
        ++ticksAlive;
        if (ticksExisted > maxTicksToLive)
        {
            setDead();
            return;
        }

        if (!worldObj.isRemote && !worldObj.blockExists((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ)))
        {
            setDead();
            return;
        }

        super.onUpdate();

        Vec3 dir = Vec3.createVectorHelper(motionX, motionY, motionZ).normalize();
        Vec3 rayStart = Vec3.createVectorHelper(//
                posX - dir.xCoord * height, //
                posY - dir.yCoord * height, //
                posZ - dir.zCoord * height);
        Vec3 rayEnd = Vec3.createVectorHelper(//
                posX + dir.xCoord * height + motionX, //
                posY + dir.yCoord * height + motionY, //
                posZ + dir.zCoord * height + motionZ);

        // Check block collision
        MovingObjectPosition collision = worldObj.rayTraceBlocks(Vec3.createVectorHelper(posX, posY, posZ),
                Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ));
        if (collision != null)
            rayEnd = Vec3.createVectorHelper(collision.hitVec.xCoord, collision.hitVec.yCoord, collision.hitVec.zCoord);

        // Check entity collisions
        @SuppressWarnings("unchecked")
        List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        Entity collidedEntity = null;
        for (Entity entity : entities)
        {
            if (!entity.canBeCollidedWith())
                continue;
            if (!canHitSource() && (!entity.isEntityEqual(shootingEntity) || ticksAlive >= 25))
                continue;

            float f = 0.3F;
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f, f, f);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(rayStart, rayEnd);
            if (movingobjectposition1 != null)
            {
                double d1 = rayStart.distanceTo(movingobjectposition1.hitVec);

                if (d1 < d0 || d0 == 0.0D)
                {
                    collidedEntity = entity;
                    d0 = d1;
                }
            }
        }
        if (collidedEntity != null)
            collision = new MovingObjectPosition(collidedEntity);
        if (collision != null)
            onImpact(collision);

        Utils.setEntityHeading(this);

        float motionFactor = getMotionFactor();
        if (isInWater())
        {
            for (int j = 0; j < 4; ++j)
            {
                float f3 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * f3, posY - motionY * f3, posZ - motionZ * f3, motionX, motionY, motionZ);
            }
            motionFactor = 0.8F;
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;

        motionX += accelerationX;
        motionY += accelerationY;
        motionZ += accelerationZ;

        motionX *= motionFactor;
        motionY *= motionFactor;
        motionZ *= motionFactor;

        setPosition(posX, posY, posZ);
    }

    /* ------------------------------------------------------------ */

    public boolean canHitSource()
    {
        return true;
    }

    public float getMotionFactor()
    {
        return 1;
    }

    public float getDamage()
    {
        return 1;
    }

    public String getDamageSourceName()
    {
        return "magic";
    }

    public DamageSource getDamageSource(Entity hurt, Entity cause)
    {
        return new EntityDamageSourceIndirect(getDamageSourceName(), hurt, cause);
    }

    /* ------------------------------------------------------------ */

    protected void onImpact(MovingObjectPosition mop)
    {
        if (!worldObj.isRemote)
        {
            if (mop.entityHit != null)
            {
                DamageSource dmgSrc = getDamageSource(this, shootingEntity);
                if (mop.entityHit.attackEntityFrom(dmgSrc, getDamage()))
                {
                    onImpactEntity(mop.entityHit);
                }
            }
            else
            {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;
                switch (mop.sideHit)
                {
                case 0:
                    --y;
                    break;
                case 1:
                    ++y;
                    break;
                case 2:
                    --z;
                    break;
                case 3:
                    ++z;
                    break;
                case 4:
                    --x;
                    break;
                case 5:
                    ++x;
                }
                if (worldObj.isAirBlock(x, y, z))
                    onImpactBlock(x, y, z);
            }
            setDead();
        }
    }

    public void onImpactBlock(int x, int y, int z)
    {
    }

    public void onImpactEntity(Entity entity)
    {
    }

    /* ------------------------------------------------------------ */

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public float getCollisionBorderSize()
    {
        return 1.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double d = boundingBox.getAverageEdgeLength() * 4 * 64;
        return distance < d * d;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (isEntityInvulnerable())
            return false;
        setBeenAttacked();

        if (source.getEntity() == null)
            return false;

        Vec3 lookVec = source.getEntity().getLookVec();
        if (lookVec != null)
        {
            motionX = lookVec.xCoord;
            motionY = lookVec.yCoord;
            motionZ = lookVec.zCoord;
            accelerationX = motionX * 0.1D;
            accelerationY = motionY * 0.1D;
            accelerationZ = motionZ * 0.1D;
        }

        if (source.getEntity() instanceof EntityLivingBase)
            this.shootingEntity = (EntityLivingBase) source.getEntity();
        return true;
    }

}