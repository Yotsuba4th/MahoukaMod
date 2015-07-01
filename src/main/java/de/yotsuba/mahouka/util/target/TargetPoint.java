package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.util.BufUtils;

public class TargetPoint extends Target
{

    protected Vec3 point;

    public TargetPoint(Vec3 point)
    {
        this.point = point;
    }

    public TargetPoint(ByteBuf buf)
    {
        point = Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        BufUtils.writeVec3(buf, point);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.POINT;
    }

    @Override
    public boolean matchesType(TargetType matchingType)
    {
        return matchingType == TargetType.POINT;
    }

    @Override
    public TargetPoint toTargetPoint()
    {
        return this;
    }

    @Override
    public Target offset(Vec3 offset)
    {
        return new TargetOffset(point.addVector(offset.xCoord, offset.yCoord, offset.zCoord), this);
    }

    public Vec3 getPoint()
    {
        return point;
    }

}