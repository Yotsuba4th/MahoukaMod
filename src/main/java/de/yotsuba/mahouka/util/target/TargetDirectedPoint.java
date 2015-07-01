package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.util.BufUtils;

public class TargetDirectedPoint extends TargetPoint
{

    private Vec3 sourcePoint;

    public TargetDirectedPoint(Vec3 point, Vec3 sourcePoint)
    {
        super(point);
        this.sourcePoint = sourcePoint;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        BufUtils.writeVec3(buf, sourcePoint);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.POINT_DIR;
    }

    @Override
    public boolean matchesType(TargetType matchingType)
    {
        switch (matchingType)
        {
        case POINT:
        case POINT_DIR:
            return true;
        default:
            return false;
        }
    }

    public Vec3 getSourcePoint()
    {
        return sourcePoint;
    }

}