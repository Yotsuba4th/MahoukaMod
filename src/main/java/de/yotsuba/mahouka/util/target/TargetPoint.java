package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.MathUtils;

public class TargetPoint extends Target
{

    protected Vec3 point;

    public TargetPoint(World world, Vec3 point)
    {
        super(world);
        this.point = point;
    }

    public TargetPoint(World world, ByteBuf buf)
    {
        super(world);
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
    public Vec3 getPoint()
    {
        return MathUtils.copyVector(point);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TargetPoint)
        {
            TargetPoint t = (TargetPoint) obj;
            return MathUtils.equals(point, t.point);
        }
        return false;
    }

}