package de.yotsuba.mahouka.magic.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cast.CastingManager;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessParallel extends MagicProcess
{

    public static final String NBT_SEQUENCES = "seq";

    protected List<ActivationSequence> sequences = new ArrayList<ActivationSequence>();

    private List<CastingProcess> casts = new ArrayList<CastingProcess>();

    /* ------------------------------------------------------------ */

    @Override
    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = super.writeToNBT();
        NBTTagList tagProcesses = new NBTTagList();
        tag.setTag(NBT_SEQUENCES, tagProcesses);
        for (ActivationSequence seq : sequences)
            tagProcesses.appendTag(seq.writeToNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        NBTTagList tagSequences = tag.getTagList(CadBase.NBT_SEQUENCES, 10);
        sequences.clear();
        for (int i = 0; i < tagSequences.tagCount(); i++)
        {
            NBTTagCompound tagSequence = tagSequences.getCompoundTagAt(i);
            sequences.add(new ActivationSequence(tagSequence));
        }
    }

    @Override
    public String getName()
    {
        return "parallel";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_parallel";
    }

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public void addInformation(List<String> info, boolean isSequence)
    {
        if (isSequence)
            info.add(StatCollector.translateToLocal("item." + getItemName() + ".name"));
        for (ActivationSequence seq : sequences)
            seq.addParallelInformation(info);
    }

    @Override
    public int getPsionCost()
    {
        int psionCost = 0;
        for (ActivationSequence sequence : sequences)
            psionCost += CastingManager.getPsionCost(sequence);
        return psionCost;
    }

    @Override
    public int getChannelingDuration()
    {
        int t = 0;
        for (ActivationSequence sequence : sequences)
            t += CastingManager.getChannelingDuration(sequence);
        return t * 4 / (3 + sequences.size());
    }

    @Override
    public int getCastDuration(Target target)
    {
        cleanCasts();
        return casts.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

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
    public Target castStart(CastingProcess cp, Target target)
    {
        casts.clear();
        for (ActivationSequence sequence : sequences)
        {
            UUID id = UUID.randomUUID();
            CastingProcess cast = new CastingProcess(cp.getCaster(), sequence, target, id, 0, 0);
            MahoukaMod.getCastingManagerServer().startChanneling(cast);
            casts.add(cast);
        }
        return target;
    }

}
