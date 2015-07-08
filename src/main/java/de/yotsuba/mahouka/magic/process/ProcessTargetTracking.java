package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetTracking;

public class ProcessTargetTracking extends MagicProcess
{

    @Override
    public String getName()
    {
        return "tracking";
    }

    @Override
    public int getPsionCost()
    {
        return 0;
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
        return new TargetTracking(target);
    }

}
