package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TargetTracking extends Target
{

    protected Target source;

    public TargetTracking(Target source)
    {
        this.source = source;
    }

    public TargetTracking(World world, ByteBuf buf)
    {
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
        return TargetType.TRACKING;
    }

    @Override
    public boolean matchesType(TargetType matchingType)
    {
        switch (matchingType)
        {
        case POINT:
        case OFFSET:
        case TRACKING:
            return true;
        default:
            return false;
        }
    }

    @Override
    public Vec3 getPoint()
    {
        return getCurrentPoint();
    }

    @Override
    public Vec3 getCurrentPoint()
    {
        return source.getCurrentPoint();
    }

    public Target getSource()
    {
        return source;
    }

}