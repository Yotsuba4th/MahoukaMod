package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public abstract class Target
{

    public abstract TargetType getType();

    public abstract TargetPoint toTargetPoint();

    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(getType().ordinal());
    }

    /* ------------------------------------------------------------ */

    public static Target fromBytes(World world, ByteBuf buf)
    {
        TargetType type = TargetType.values()[buf.readByte()];
        Target target;
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
            if (((TargetEntity) target).getEntity() == null)
                return null;
            break;
        case POINT:
            target = new TargetPoint(buf);
            break;
        case AREA:
            target = new TargetArea(buf);
            break;
        case BLOCK:
            target = new TargetBlock(world, buf);
            break;
        default:
            return null;
        }
        return target;
    }

}
