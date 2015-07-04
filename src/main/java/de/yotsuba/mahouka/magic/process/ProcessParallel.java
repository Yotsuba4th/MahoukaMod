package de.yotsuba.mahouka.magic.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessParallel extends ProcessSequence
{

    private List<CastingProcess> casts = new ArrayList<CastingProcess>();

    /* ------------------------------------------------------------ */

    @Override
    public String getName()
    {
        return "parallel";
    }

    @Override
    public TargetType[] getValidTargets()
    {
        // TODO (1) Valid targets
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public void addInformation(List<String> info, boolean isSequence)
    {
        // super.addInformation(info, isSequence);
        for (MagicProcess seq : processes)
        {
            int oldLength2 = info.size() + 1;

            seq.addInformation(info, true);

            for (int i = oldLength2; i < info.size(); i++)
                info.set(i, "   " + info.get(i));
        }
    }

    @Override
    public int getPsionCost()
    {
        return super.getPsionCost();
    }

    @Override
    public int getChannelingDuration()
    {
        return super.getChannelingDuration();
    }

    @Override
    public int getCastDuration(Target target)
    {
        cleanCasts();
        return casts.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    /* ------------------------------------------------------------ */

    private void cleanCasts()
    {
        for (Iterator<CastingProcess> it = casts.iterator(); it.hasNext();)
        {
            CastingProcess cast = it.next();
            if (!cast.isActive())
                it.remove();
        }
    }

    @Override
    public boolean castCancel(CastingProcess cp, Target target)
    {
        cleanCasts();
        if (casts.isEmpty())
            return true;
        boolean cancelledAny = false;
        for (CastingProcess castingProcess : casts)
            cancelledAny |= castingProcess.cancel(false);
        cleanCasts();
        return cancelledAny;
    }

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        casts.clear();
        for (MagicProcess sequence : processes)
        {
            UUID id = UUID.randomUUID();
            CastingProcess cast = new CastingProcess(cp.getCaster(), sequence, target, id, 0, 0);
            cast.start();
            casts.add(cast);
        }
        return target;
    }

}
