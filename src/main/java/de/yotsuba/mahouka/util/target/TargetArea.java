package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.MathUtils;
import de.yotsuba.mahouka.util.Shape;

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
        BufUtils.writeVec3(buf, center);
        BufUtils.writeVec3(buf, size);
        buf.writeByte(shape.ordinal());
    }

    @Override
    public Vec3 getPoint()
    {
        // TODO (2) Get random point!
        return MathUtils.copyVector(center);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.AREA;
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

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TargetArea)
        {
            TargetArea t = (TargetArea) obj;
            return MathUtils.equals(center, t.center) && MathUtils.equals(size, t.size) && shape == t.shape;
        }
        return false;
    }

}