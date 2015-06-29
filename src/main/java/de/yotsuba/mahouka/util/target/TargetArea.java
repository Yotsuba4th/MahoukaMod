package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.util.Shape;
import de.yotsuba.mahouka.util.Utils;

public class TargetArea extends Target
{

    private Vec3 center;

    private Vec3 size;

    private Shape shape;

    public TargetArea(ByteBuf buf)
    {
        center = Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble());
        size = Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble());
        shape = Shape.values()[buf.readByte()];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        Utils.writeVec3(buf, center);
        Utils.writeVec3(buf, size);
        buf.writeByte(shape.ordinal());
    }

    @Override
    public TargetType getType()
    {
        return TargetType.AREA;
    }

    @Override
    public TargetPoint toTargetPoint()
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