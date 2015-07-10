package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

// TODO (2) Implement equals
public abstract class Target
{

    protected World world;

    public Target(World world)
    {
        this.world = world;
    }

    public abstract Vec3 getPoint();

    public World getWorld()
    {
        return world;
    }

    public Vec3 getCurrentPoint()
    {
        return getPoint();
    }

    public abstract TargetType getType();

    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(getType().ordinal());
    }

    @Override
    public abstract boolean equals(Object obj);

    /* ------------------------------------------------------------ */

    public static Target fromBytes(World world, ByteBuf buf)
    {
        TargetType type = TargetType.values()[buf.readByte()];
        Target target = null;
        switch (type)
        {
        case ANIMAL:
        case ENTITY:
        case PLAYER:
        case ITEM:
        case LIVING:
        case MOB:
        case SELF:
            target = new TargetEntity(world, buf, type);
            break;
        case POINT:
            target = new TargetPoint(world, buf);
            break;
        case OFFSET:
            target = new TargetOffset(world, buf);
            break;
        case TRACKING:
            target = new TargetTracking(world, buf);
            break;
        case AHEAD:
            target = new TargetAhead(world, buf);
            break;
        case AREA:
            target = new TargetArea(world, buf);
            break;
        case BLOCK:
            target = new TargetBlock(world, buf);
            break;
        }
        return target;
    }

}
