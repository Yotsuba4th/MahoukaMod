package de.yotsuba.mahouka.util;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.util.Vec3;

public class BufUtils
{

    public static UUID uuidFromBytes(ByteBuf buf)
    {
        long hi = buf.readLong();
        long lo = buf.readLong();
        return new UUID(hi, lo);
    }

    public static void uuidToBytes(ByteBuf buf, UUID uuid)
    {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public static void writeVec3(ByteBuf buf, Vec3 vec)
    {
        buf.writeDouble(vec.xCoord);
        buf.writeDouble(vec.yCoord);
        buf.writeDouble(vec.zCoord);
    }
    
}
