package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TargetMovingOffset extends TargetOffset
{

    public TargetMovingOffset(Target source, Vec3 offset)
    {
        super(source, offset);
    }

    public TargetMovingOffset(World world, ByteBuf buf)
    {
        super(world, buf);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.MOVING_OFFSET;
    }

    @Override
    public Vec3 getCurrentPoint()
    {
        return source.getCurrentPoint().addVector(offset.xCoord, offset.yCoord, offset.zCoord);
    }

}