package de.yotsuba.mahouka.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import de.yotsuba.mahouka.MahoukaMod;

public class ActivationSequence
{

    public static final String NBT_PROCESSES = "proc";

    /* ------------------------------------------------------------ */

    protected List<MagicProcess> processes = new ArrayList<MagicProcess>();

    /* ------------------------------------------------------------ */

    public ActivationSequence()
    {
    }

    public ActivationSequence(NBTTagCompound tagSequence)
    {
        readFromNBT(tagSequence);
    }

    /* ------------------------------------------------------------ */

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
            MagicProcess process = MagicProcess.createFromNBT(tagProcess);
            if (process == null)
            {
                // TODO: DEBUG! Print log error!
                System.err.println("Could not load magic process!");
                break;
            }
            processes.add(process);
        }
    }

    /* ------------------------------------------------------------ */

    public static void registerIcons()
    {
        // TODO: Register additional icons for special sequences
        // ItemMagicSequence.registerIcon("name");
    }

    public String getIcon()
    {
        if (processes.size() == 1)
            return processes.get(0).getTextureName();
        return null;
    }

    public void addInformation(List<String> info)
    {
        if (processes.isEmpty())
        {
            info.add("Error! Emtpy sequence!");
            return;
        }

        info.add("Psion cost  : " + getPsionCost());
        info.add("Channel time: " + getChannelingDuration());

        int oldLength1 = info.size();
        if (processes.size() == 1)
        {
            processes.get(0).addInformation(info, false);
        }
        else
        {
            for (MagicProcess process : processes)
                process.addInformation(info, true);
        }
        for (int i = oldLength1; i < info.size(); i++)
            info.set(i, "  " + info.get(i));
    }

    /* ------------------------------------------------------------ */

    public int getPsionCost()
    {
        int psionCost = 0;
        for (MagicProcess process : processes)
            psionCost += process.getPsionCost();
        return psionCost;
    }

    public int getChannelingDuration()
    {
        int t = 0;
        for (MagicProcess process : processes)
            t += process.getChannelingDuration();
        return t;
    }

    public List<MagicProcess> getProcesses()
    {
        return processes;
    }

    public ItemStack getItemStack()
    {
        if (processes.isEmpty())
            return null;

        NBTTagCompound tag = writeToNBT();
        ItemStack stack = new ItemStack(processes.size() == 1 ? processes.get(0).getItem() : MahoukaMod.item_magic_sequence);
        stack.setTagCompound(tag);
        return stack;
    }

}
