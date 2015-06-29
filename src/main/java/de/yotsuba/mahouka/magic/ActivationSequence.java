package de.yotsuba.mahouka.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import de.yotsuba.mahouka.magic.process.MagicProcess;
import de.yotsuba.mahouka.magic.process.MagicProcessManager;

public class ActivationSequence
{

    public static final String NBT_PROCESSES = "proc";
    
    protected List<MagicProcess> processes = new ArrayList<MagicProcess>();

    public ActivationSequence()
    {
    }

    public ActivationSequence(NBTTagCompound tagSequence)
    {
        readFromNBT(tagSequence);
    }

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList tagProcesses = new NBTTagList();
        tag.setTag(NBT_PROCESSES, tagProcesses);
        for (MagicProcess process : processes)
            tagProcesses.appendTag(process.writeToNBT());
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        NBTTagList tagProcesses = tag.getTagList(NBT_PROCESSES, 10);
        for (int i = 0; i < tagProcesses.tagCount(); i++)
        {
            NBTTagCompound tagProcess = tagProcesses.getCompoundTagAt(i);
            processes.add(MagicProcessManager.readFromNBT(tagProcess));
        }
    }

    public void getCasterChannelingEffects(/* cast target */)
    {
    }

    public void getTargetChannelingEffects(/* cast target */)
    {
    }

    public List<MagicProcess> getProcesses()
    {
        return processes;
    }

}
