package de.yotsuba.mahouka.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconReg)
    {
        // TODO: Register additional icons for special sequences
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        if (processes.size() == 1)
            return processes.get(0).getIcon();
        return null;
    }

    public void addInformation(List<String> info)
    {
        if (processes.isEmpty())
        {
            info.add("Error! Emtpy sequence!");
        }
        else if (processes.size() == 1)
        {
            processes.get(0).addInformation(info, false);
        }
        else
        {
            for (MagicProcess process : processes)
            {
                process.addInformation(info, true);
            }
        }
    }

    /* ------------------------------------------------------------ */

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
