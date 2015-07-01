package de.yotsuba.mahouka.magic.process;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessParallel extends MagicProcess
{

    public static final String NBT_SEQUENCES = "seq";

    protected List<ActivationSequence> sequences = new ArrayList<ActivationSequence>();

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
    public int getChannelingDuration()
    {
        return 0;
    }

    @Override
    public int getCastDuration(Target target)
    {
        // TODO: Return Integer.MAX_VALUE until all sub-casts finished
        return 0;
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
    public Target cast(CastingProcess cp, Target target)
    {
        // TODO: Spawn sub-casts with new UUIDs and remember them in a list

        return target;
    }

}
