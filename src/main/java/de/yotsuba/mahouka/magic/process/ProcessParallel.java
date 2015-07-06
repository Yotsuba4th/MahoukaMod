package de.yotsuba.mahouka.magic.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;

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
    public boolean isTargetValid(Target target)
    {
        for (MagicProcess process : processes)
            if (!process.isTargetValid(target))
                return false;
        return true;
    }

    @Override
    public void addInformation(List<String> info, boolean isRoot)
    {
        // super.addInformation(info, isSequence);
        if (isRoot)
        {
            info.add("Psion cost  : " + getPsionCost());
            info.add("Channel time: " + getChannelingDuration());
        }
        for (MagicProcess seq : processes)
        {
            int oldSize = info.size();
            seq.addInformation(info, false);
            for (int i = oldSize + 1; i < info.size(); i++)
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
        return super.getChannelingDuration() / 2;
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
