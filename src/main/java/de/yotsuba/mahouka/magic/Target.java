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

    public static enum Type
    {
        ENTITY, ITEM, LIVING, ANIMAL, MOB, PLAYER, SELF, BLOCK, AREA, POINT;
    }

    public abstract Type getType();

    public static class TargetEntity extends Target
    {

        private Entity entity;

        private Type type;

        private boolean isConstructed;

        public TargetEntity(Entity entity, boolean isSelf, boolean isConstructed)
        {
            this.entity = entity;
            this.isConstructed = isConstructed;
            if (entity instanceof EntityPlayer)
                type = isSelf ? Type.SELF : Type.PLAYER;
            else if (entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)
                type = Type.ANIMAL;
            else if (entity instanceof EntityMob || entity instanceof EntityDragon || entity instanceof EntityFlying || entity instanceof EntitySlime)
                type = Type.MOB;
            else if (entity instanceof EntityLivingBase)
                type = Type.LIVING;
            else if (entity instanceof EntityItem || entity instanceof EntityXPOrb)
                type = Type.ITEM;
            else
                type = Type.ENTITY;
        }

        @Override
        public Type getType()
        {
            return type;
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
        public Type getType()
        {
            return Type.BLOCK;
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
        public Type getType()
        {
            return Type.POINT;
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
        public Type getType()
        {
            return Type.AREA;
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
