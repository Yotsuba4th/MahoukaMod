package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.MathUtils;

public class TargetOffset extends Target
{

    protected Target source;

    protected Vec3 offset;

    public TargetOffset(Target source, Vec3 offset)
    {
        super(source.getWorld());
        this.source = source;
        this.offset = offset;
    }

    public TargetOffset(World world, ByteBuf buf)
    {
        super(world);
        source = Target.fromBytes(world, buf);
        offset = Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        source.toBytes(buf);
        BufUtils.writeVec3(buf, offset);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.OFFSET;
    }

    @Override
    public Vec3 getPoint()
    {
        return source.getPoint().addVector(offset.xCoord, offset.yCoord, offset.zCoord);
    }

    @Override
    public Vec3 getCurrentPoint()
    {
        return source.getCurrentPoint().addVector(offset.xCoord, offset.yCoord, offset.zCoord);
    }

    public Target getSource()
    {
        return source;
    }

    public Vec3 getOffset()
    {
        return offset;
    }

    public Vec3 getSourcePoint()
    {
        return source.getPoint();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TargetOffset)
        {
            TargetOffset t = (TargetOffset) obj;
            return source.equals(t.source) && MathUtils.equals(offset, t.offset);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return source.hashCode() * 89 + offset.hashCode();
    }

}