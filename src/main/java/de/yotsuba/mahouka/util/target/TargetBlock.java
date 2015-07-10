package de.yotsuba.mahouka.util.target;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TargetBlock extends TargetPoint
{

    private int x;

    private int y;

    private int z;

    private Block block;

    public TargetBlock(World world, ByteBuf buf)
    {
        super(world, buf);
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        block = world.getBlock(x, y, z);
    }

    public TargetBlock(World world, int x, int y, int z, Vec3 point)
    {
        super(world, point);
        this.x = x;
        this.y = y;
        this.z = z;
        block = world.getBlock(x, y, z);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public TargetType getType()
    {
        return TargetType.BLOCK;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public Block getBlock()
    {
        return block;
    }

}