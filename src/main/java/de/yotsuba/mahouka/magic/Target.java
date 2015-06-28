package de.yotsuba.mahouka.magic;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.util.Shape;

public abstract class Target
{

    public static enum TargetType
    {
        ENTITY, ITEM, LIVING, ANIMAL, MOB, PLAYER, SELF, BLOCK, AREA, POINT;
    }

    public abstract TargetType getType();

    public abstract TargetPoint toPoint();

    public static class TargetEntity extends Target
    {

        private Entity entity;

        private TargetType type;

        private boolean isConstructed;

        public TargetEntity(Entity entity, boolean isSelf, boolean isConstructed)
        {
            this.entity = entity;
            this.isConstructed = isConstructed;
            if (entity instanceof EntityPlayer)
                type = isSelf ? TargetType.SELF : TargetType.PLAYER;
            else if (entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)
                type = TargetType.ANIMAL;
            else if (entity instanceof EntityMob || entity instanceof EntityDragon || entity instanceof EntityFlying || entity instanceof EntitySlime)
                type = TargetType.MOB;
            else if (entity instanceof EntityLivingBase)
                type = TargetType.LIVING;
            else if (entity instanceof EntityItem || entity instanceof EntityXPOrb)
                type = TargetType.ITEM;
            else
                type = TargetType.ENTITY;
        }

        @Override
        public TargetType getType()
        {
            return type;
        }

        @Override
        public TargetPoint toPoint()
        {
            return new TargetPoint(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ));
        }

        public Entity getEntity()
        {
            return entity;
        }

        public EntityPlayer getPlayer()
        {
            return (EntityPlayer) entity;
        }

        /**
         * Was the entity constructed as part of the magic sequence?
         */
        public boolean isConstructed()
        {
            return isConstructed;
        }

    }

    public static class TargetBlock extends Target
    {

        private int x;

        private int y;

        private int z;

        private Block block;

        @Override
        public TargetType getType()
        {
            return TargetType.BLOCK;
        }

        @Override
        public TargetPoint toPoint()
        {
            return new TargetPoint(Vec3.createVectorHelper(x, y, z));
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public int getZ()
        {
            return z;
        }

        public Block getBlock()
        {
            return block;
        }

    }

    public static class TargetPoint extends Target
    {

        private Vec3 point;

        public TargetPoint(Vec3 point)
        {
            this.point = point;
        }

        @Override
        public TargetType getType()
        {
            return TargetType.POINT;
        }

        @Override
        public TargetPoint toPoint()
        {
            return this;
        }

        public Vec3 getPoint()
        {
            return point;
        }

    }

    public static class TargetDirectedPoint extends TargetPoint
    {

        private Vec3 sourcePoint;

        public TargetDirectedPoint(Vec3 point, Vec3 sourcePoint)
        {
            super(point);
            this.sourcePoint = sourcePoint;
        }

        @Override
        public TargetPoint toPoint()
        {
            return this;
        }

        public Vec3 getSourcePoint()
        {
            return sourcePoint;
        }

    }

    public static class TargetArea extends Target
    {

        private Vec3 center;

        private Vec3 size;

        private Shape shape;

        @Override
        public TargetType getType()
        {
            return TargetType.AREA;
        }

        @Override
        public TargetPoint toPoint()
        {
            // TODO: Get random point!
            return new TargetPoint(center);
        }

        public Vec3 getCenter()
        {
            return center;
        }

        public Vec3 getSize()
        {
            return size;
        }

        public Shape getShape()
        {
            return shape;
        }

    }

}
