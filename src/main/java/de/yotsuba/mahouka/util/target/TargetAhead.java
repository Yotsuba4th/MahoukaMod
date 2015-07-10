package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import de.yotsuba.mahouka.core.PlayerMotionTracker;
import de.yotsuba.mahouka.util.MathUtils;

public class TargetAhead extends Target
{

    protected TargetEntity source;

    protected float offset;

    public TargetAhead(TargetEntity source, float offset)
    {
        super(source.getWorld());
        this.source = source;
        this.offset = offset;
    }

    public TargetAhead(World world, ByteBuf buf)
    {
        super(world);
        source = (TargetEntity) Target.fromBytes(world, buf);
        offset = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        source.toBytes(buf);
        buf.writeFloat(offset);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.AHEAD;
    }

    public Vec3 getPoint(Vec3 sourcePoint)
    {
        Entity entity = source.getEntity();
        Vec3 offsetVec = PlayerMotionTracker.getEntityMotion(entity);
        double speed = offsetVec.lengthVector();
        if (speed < 0.08)
            return sourcePoint;
        MathUtils.scaleVector(offsetVec, offset / speed);
        return sourcePoint.addVector(offsetVec.xCoord, offsetVec.yCoord, offsetVec.zCoord);
    }

    @Override
    public Vec3 getPoint()
    {
        return getPoint(source.getPoint());
    }

    @Override
    public Vec3 getCurrentPoint()
    {
        return getPoint(source.getCurrentPoint());
    }

    public TargetEntity getSource()
    {
        return source;
    }

    public float getOffset()
    {
        return offset;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TargetAhead)
        {
            TargetAhead t = (TargetAhead) obj;
            return source.equals(t.source) && offset == t.offset;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return source.hashCode() * 89 + Float.hashCode(offset);
    }

}