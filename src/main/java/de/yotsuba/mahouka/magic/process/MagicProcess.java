package de.yotsuba.mahouka.magic.process;

import java.util.HashSet;
import java.util.Set;

import de.yotsuba.mahouka.magic.Target;

public abstract class MagicProcess
{

    public void getCasterEffect(/* cast target */)
    {
    }

    public void getTargetChannelingEffect(/* cast target */)
    {
    }

    public void getTargetCastingEffect(/* cast target */)
    {
    }

    public Set<Target.Type> getValidTargets()
    {
        return new HashSet<Target.Type>();
    }

    public boolean isContinuousCast()
    {
        return false;
    }

    public int getCastDuration(/* cast target */)
    {
        return 0;
    }

    public void cast(/* cast target */)
    {
    }

}
