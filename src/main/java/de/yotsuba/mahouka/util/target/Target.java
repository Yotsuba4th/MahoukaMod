package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class Target
{

    public abstract Vec3 getPoint();

    public Vec3 getCurrentPoint()
    {
        return getPoint();
    }

    public abstract TargetType getType();

    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(getType().ordinal());
    }

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
            target = new TargetPoint(buf);
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
            target = new TargetArea(buf);
            break;
        case BLOCK:
            target = new TargetBlock(world, buf);
            break;
        }
        return target;
    }

}
