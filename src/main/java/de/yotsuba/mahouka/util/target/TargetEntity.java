package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
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
import net.minecraft.world.World;

public class TargetEntity extends TargetPoint
{

    protected Entity entity;

    protected TargetType type;

    protected boolean isConstructed;

    public TargetEntity(Entity entity, boolean isSelf, boolean isConstructed, Vec3 point)
    {
        super(point);
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

    public TargetEntity(Entity entity, boolean isSelf, boolean isConstructed)
    {
        this(entity, isSelf, isConstructed, Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ));
    }

    public TargetEntity(World world, ByteBuf buf, TargetType type)
    {
        super(buf);
        this.type = type;
        this.entity = world.getEntityByID(buf.readInt());
        this.isConstructed = buf.readBoolean();
    }

    private TargetEntity(Entity entity, TargetType type, boolean isConstructed, Vec3 point)
    {
        super(point);
        this.type = type;
        this.entity = entity;
        this.isConstructed = isConstructed;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(entity.getEntityId());
        buf.writeBoolean(isConstructed);
    }

    @Override
    public TargetType getType()
    {
        return type;
    }

    @Override
    public boolean matchesType(TargetType matchingType)
    {
        // Early check of equal cases - these will not be handled below
        if (type == matchingType)
            return true;
        switch (matchingType)
        {
        case ENTITY:
        case POINT:
        case OFFSET:
            return true;
        case LIVING:
            switch (type)
            {
            case ANIMAL:
            case LIVING:
            case MOB:
            case PLAYER:
            case SELF:
                return true;
            default:
                return false;
            }
        case PLAYER:
            switch (type)
            {
            case PLAYER:
            case SELF:
                return true;
            default:
                return false;
            }
        case SELF:
        case AREA:
        case ITEM:
        case BLOCK:
        case ANIMAL:
        case MOB:
        default:
            return false;
        }
    }

    public Vec3 getCurrentPoint()
    {
        if (entity == null)
            return getPoint();
        return Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
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