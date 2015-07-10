package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TargetTracking extends Target
{

    protected Target source;

    public TargetTracking(Target source)
    {
        super(source.getWorld());
        this.source = source;
    }

    public TargetTracking(World world, ByteBuf buf)
    {
        super(world);
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

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TargetTracking)
        {
            TargetTracking t = (TargetTracking) obj;
            return source.equals(t.source);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return source.hashCode() * 89 + 7;
    }

}