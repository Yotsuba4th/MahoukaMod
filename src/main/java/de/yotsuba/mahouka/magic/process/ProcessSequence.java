package de.yotsuba.mahouka.magic.process;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.util.target.Target;

public class ProcessSequence extends MagicProcess
{

    public static final String NBT_SEQUENCES = "seq";

    /* ------------------------------------------------------------ */

    protected List<MagicProcess> processes = new ArrayList<MagicProcess>();

    /* ------------------------------------------------------------ */

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        NBTTagList tagProcesses = new NBTTagList();
        tag.setTag(NBT_SEQUENCES, tagProcesses);
        for (MagicProcess sequence : processes)
        {
            NBTTagCompound tagSequence = new NBTTagCompound();
            sequence.getItemStack().writeToNBT(tagSequence);
            // sequence.writeToNBT(tagSequence);
            tagProcesses.appendTag(tagSequence);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList tagSequences = tag.getTagList(NBT_SEQUENCES, 10);
        processes.clear();
        for (int i = 0; i < tagSequences.tagCount(); i++)
        {
            NBTTagCompound tagSequence = tagSequences.getCompoundTagAt(i);
            MagicProcess process = MagicProcess.createFromStack(ItemStack.loadItemStackFromNBT(tagSequence));
            if (process == null)
            {
                // MahoukaMod.getLogger().error("Could not load magic process!");
                new RuntimeException("Could not load magic process!").printStackTrace();
                break;
            }
            processes.add(process);
        }
    }

    /* ------------------------------------------------------------ */

    @Override
    public String getName()
    {
        return "sequence";
    }

    @Override
    public Item getItem()
    {
        return MahoukaMod.item_magic_sequence;
    }

    @Override
    public boolean isTargetValid(Target target)
    {
        if (processes.isEmpty())
            return false;
        return processes.get(0).isTargetValid(target);
    }

    @Override
    public void addInformation(List<String> info, boolean isRoot)
    {
        if (isRoot)
        {
            info.add("Psion cost  : " + getPsionCost());
            info.add("Channel time: " + getChannelingDuration());
        }

        int oldLength1 = info.size();
        if (processes.size() == 1)
        {
            processes.get(0).addInformation(info, false);
        }
        else
        {
            for (MagicProcess process : processes)
                process.addInformation(info, false);
        }
        for (int i = oldLength1; i < info.size(); i++)
            info.set(i, "  " + info.get(i));
    }

    @Override
    public int getPsionCost()
    {
        int psionCost = 0;
        for (MagicProcess process : processes)
            psionCost += process.getPsionCost();
        return psionCost;
    }

    @Override
    public int getChannelingDuration()
    {
        int t = 0;
        for (MagicProcess process : processes)
            t += process.getChannelingDuration();
        return t;
    }

    @Override
    public int getCastDuration(Target target)
    {
        return 0;
        // cleanCasts();
        // return casts.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    public List<MagicProcess> getProcesses()
    {
        return processes;
    }

    /* ------------------------------------------------------------ */

}
