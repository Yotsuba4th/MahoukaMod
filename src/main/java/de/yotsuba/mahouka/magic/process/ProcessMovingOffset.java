package de.yotsuba.mahouka.magic.process;

import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetMovingOffset;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessMovingOffset extends ProcessOffset
{

    private Vec3 offset;

    public ProcessMovingOffset()
    {
        offset = Vec3.createVectorHelper(0, 5, 0);
    }

    @Override
    public String getName()
    {
        return "offset_moving";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_offset_moving";
    }

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public int getChannelingDuration()
    {
        return 0;
    }

    @Override
    public int getCastDuration(Target target)
    {
        return 0;
    }

    @Override
    public Target cast(CastingProcess cp, Target target)
    {
        return new TargetMovingOffset(target, offset);
    }

}