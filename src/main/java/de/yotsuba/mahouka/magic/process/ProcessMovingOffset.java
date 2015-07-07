package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetMovingOffset;

public class ProcessMovingOffset extends ProcessOffset
{

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
    public Target castStart(CastingProcess cp, Target target)
    {
        return new TargetMovingOffset(target, getRandomOffset());
    }

}
