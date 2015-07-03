package de.yotsuba.mahouka.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectile;

public class WorldUtils
{

    /* ------------------------------------------------------------ */

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public static MovingObjectPosition rayTraceTarget(double maxDistance)
    {
        return rayTraceClient(Minecraft.getMinecraft().renderViewEntity, maxDistance, new Class[] { EntityMagicProjectile.class });
    }

    @SideOnly(Side.CLIENT)
    public static MovingObjectPosition rayTraceClient(double maxDistance, boolean ignoreNonClollidables)
    {
        return rayTraceClient(Minecraft.getMinecraft().renderViewEntity, maxDistance, ignoreNonClollidables);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public static MovingObjectPosition rayTraceClient(EntityLivingBase viewEntity, double maxDistance, boolean ignoreNonClollidables)
    {
        return rayTraceClient(viewEntity, maxDistance, ignoreNonClollidables ? null : new Class[] {});
    }

    @SideOnly(Side.CLIENT)
    public static MovingObjectPosition rayTraceClient(EntityLivingBase viewEntity, double maxDistance, Class<? extends Entity>[] ignoreNonClollidables)
    {
        float timeOffset = 1;
        Vec3 start = viewEntity.getPosition(timeOffset);
        Vec3 dir = viewEntity.getLook(timeOffset);
        return rayTrace(viewEntity, maxDistance, start, dir, ignoreNonClollidables);
    }

    /* ------------------------------------------------------------ */

    @SuppressWarnings("unchecked")
    public static MovingObjectPosition rayTraceServer(EntityLivingBase viewEntity, double maxDistance, boolean ignoreNonClollidables)
    {
        return rayTraceServer(viewEntity, maxDistance, ignoreNonClollidables ? null : new Class[] {});
    }

    public static MovingObjectPosition rayTraceServer(EntityLivingBase viewEntity, double maxDistance, Class<? extends Entity>[] ignoreNonClollidables)
    {
        float timeOffset = 1;
        Vec3 start = Vec3.createVectorHelper(viewEntity.posX, viewEntity.posY + viewEntity.getEyeHeight() - 0.12, viewEntity.posZ);
        Vec3 dir = viewEntity.getLook(timeOffset);
        return rayTrace(viewEntity, maxDistance, start, dir, ignoreNonClollidables);
    }

    /* ------------------------------------------------------------ */

    public static MovingObjectPosition rayTrace(EntityLivingBase viewEntity, double maxDistance, Vec3 start, Vec3 dir,
            Class<? extends Entity>[] ignoreNonClollidables)
    {
        Vec3 rayEnd = start.addVector(dir.xCoord * maxDistance, dir.yCoord * maxDistance, dir.zCoord * maxDistance);

        // First check block hit
        Vec3 blockStart = Vec3.createVectorHelper(start.xCoord, start.yCoord, start.zCoord);// viewEntity.getPosition(1);
        MovingObjectPosition result = viewEntity.worldObj.func_147447_a(blockStart, rayEnd, false, false, true);
        double blockHitDistance = maxDistance;
        if (result != null)
            blockHitDistance = result.hitVec.distanceTo(start);

        // Get entities in the area from start to end
        AxisAlignedBB aabb = viewEntity.boundingBox.addCoord(dir.xCoord * maxDistance, dir.yCoord * maxDistance, dir.zCoord * maxDistance);
        aabb = aabb.expand(1, 1, 1);
        @SuppressWarnings("unchecked")
        List<Entity> entities = viewEntity.worldObj.getEntitiesWithinAABBExcludingEntity(viewEntity, aabb);

        // Check for collisions with entities
        double minD = blockHitDistance;
        Entity pointedEntity = null;
        Vec3 hitVec = null;
        for (Entity entity : entities)
        {
            if (ignoreNonClollidables != null)
            {
                boolean ignore = false;
                for (Class<? extends Entity> clazz : ignoreNonClollidables)
                    if (clazz.isAssignableFrom(entity.getClass()))
                    {
                        ignore = true;
                        break;
                    }
                if (ignore)
                    continue;
            }
            else
            {
                if (!entity.canBeCollidedWith())
                    continue;
            }

            float size = 0.4f; // entity.getCollisionBorderSize() + 0.5f;
            AxisAlignedBB entityAABB = entity.boundingBox.expand(size, size, size);
            MovingObjectPosition entityHit = entityAABB.calculateIntercept(start, rayEnd);

            if (entityAABB.isVecInside(start))
            {
                if (0.0D < minD || minD == 0.0D)
                {
                    pointedEntity = entity;
                    hitVec = (entityHit == null) ? start : entityHit.hitVec;
                    minD = 0.0D;
                }
            }
            else if (entityHit != null)
            {
                double dist = start.distanceTo(entityHit.hitVec);
                if (dist < minD || minD == 0.0D)
                {
                    if (entity == viewEntity.ridingEntity && !entity.canRiderInteract())
                    {
                        if (minD == 0.0D)
                        {
                            pointedEntity = entity;
                            hitVec = entityHit.hitVec;
                        }
                    }
                    else
                    {
                        pointedEntity = entity;
                        hitVec = entityHit.hitVec;
                        minD = dist;
                    }
                }
            }
        }
        if (pointedEntity != null && (minD < blockHitDistance || result == null))
        {
            result = new MovingObjectPosition(pointedEntity, hitVec);
        }
        return result;
    }

    /* ------------------------------------------------------------ */

    public static int getBrightnessForRender(Entity entity)
    {
        int x = MathHelper.floor_double(entity.posX);
        int z = MathHelper.floor_double(entity.posZ);
        if (entity.worldObj.blockExists(x, 0, z))
        {
            double d0 = (entity.boundingBox.maxY - entity.boundingBox.minY) * 0.66D;
            int k = MathHelper.floor_double(entity.posY - entity.yOffset + d0);
            return entity.worldObj.getLightBrightnessForSkyBlocks(x, k, z, 0);
        }
        else
        {
            return 0;
        }
    }

    public static double dropOnGround(World world, double posX, double posY, double posZ)
    {
        int x = (int) Math.floor(posX);
        int y = (int) Math.floor(posY);
        int z = (int) Math.floor(posZ);
        while (y >= 0)
        {
            Block block = world.getBlock(x, y, z);
            if (block.getMaterial().isSolid())
            {
                return y + 0.5f;
            }
            y--;
        }
        return posY;
    }

}
