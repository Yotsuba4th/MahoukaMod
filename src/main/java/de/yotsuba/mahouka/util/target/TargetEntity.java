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

    private Entity entity;

    private TargetType type;

    private boolean isConstructed;

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