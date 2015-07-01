package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TargetOffset extends TargetPoint
{

    private Target source;

    public TargetOffset(Vec3 point, Target source)
    {
        super(point);
        this.source = source;
    }

    public TargetOffset(World world, ByteBuf buf)
    {
        super(buf);
        source = Target.fromBytes(world, buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        source.toBytes(buf);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.OFFSET;
    }

    @Override
    public boolean matchesType(TargetType matchingType)
    {
        switch (matchingType)
        {
        case POINT:
        case OFFSET:
            return true;
        default:
            return false;
        }
    }

    @Override
    public Target offset(Vec3 offset)
    {
        return new TargetOffset(point.addVector(offset.xCoord, offset.yCoord, offset.zCoord), source);
    }

    public Target getSource()
    {
        return source;
    }

}