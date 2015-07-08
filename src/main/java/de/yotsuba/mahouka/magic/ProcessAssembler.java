package de.yotsuba.mahouka.magic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.magic.process.ProcessParallel;
import de.yotsuba.mahouka.magic.process.ProcessSequence;

// TODO (4) Improve process assembler so it does not require exceptions any more
public class ProcessAssembler
{

    public static ItemStack combine(ItemStack input1, ItemStack input2)
    {
        if (input1 == null || input2 == null)
            return null;
        if (!(input1.getItem() instanceof ItemMagicProcess) || !(input2.getItem() instanceof ItemMagicProcess))
            return null;

        ItemMagicProcess item1 = (ItemMagicProcess) input1.getItem();
        ItemMagicProcess item2 = (ItemMagicProcess) input2.getItem();
        if (item1.getItemProcess() instanceof ProcessParallel)
        {
            ItemStack output = new ItemStack(item1);
            NBTTagCompound tag = new NBTTagCompound();
            ProcessParallel process = new ProcessParallel();

            // Add all processes of input 1
            process.getProcesses().addAll(((ProcessSequence) item1.getProcess(input1)).getProcesses());
            // Add process sequence of input 2
            process.getProcesses().add(item2.getProcess(input2));

            process.writeToNBT(tag);
            output.setTagCompound(tag);
            return output;
        }
        else
        {
            ItemStack output = new ItemStack(MahoukaMod.item_magic_sequence);
            NBTTagCompound tag = new NBTTagCompound();
            ProcessSequence process = new ProcessSequence();

            // Add all processes of input 1
            if (item1.getItemProcess() instanceof ProcessSequence)
                process.getProcesses().addAll(((ProcessSequence) item1.getProcess(input1)).getProcesses());
            else
                process.getProcesses().add(item1.getProcess(input1));

            // Add all processes of input 2
            if (item2.getItemProcess().getClass().equals(ProcessSequence.class))
                process.getProcesses().addAll(((ProcessSequence) item2.getProcess(input2)).getProcesses());
            else
                process.getProcesses().add(item2.getProcess(input2));

            process.writeToNBT(tag);
            output.setTagCompound(tag);
            return output;
        }
    }

    public static ItemStack split(ItemStack input)
    {
        if (input == null || !(input.getItem() instanceof ItemMagicProcess))
            return null;
        ItemMagicProcess item = (ItemMagicProcess) input.getItem();
        if (!(item.getItemProcess() instanceof ProcessSequence))
            return null;

        ProcessSequence sequence = (ProcessSequence) item.getProcess(input);
        if (sequence.getProcesses().size() <= 1)
            return null;

        ItemStack result = sequence.getProcesses().remove(sequence.getProcesses().size() - 1).getItemStack();
        sequence.writeToNBT(input.getTagCompound());
        return result;
    }

    public static ItemStack unwrapSequence(ItemStack input)
    {
        if (input == null || !(input.getItem() instanceof ItemMagicProcess))
            return null;

        ItemMagicProcess item = (ItemMagicProcess) input.getItem();
        if (!(item.getItemProcess() instanceof ProcessSequence))
            return input;

        ProcessSequence sequence = (ProcessSequence) item.getProcess(input);
        if (sequence.getProcesses().size() == 1)
            return sequence.getProcesses().get(0).getItemStack();
        else
            return input;
    }

}
