package de.yotsuba.mahouka.magic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.item.ItemMagicSequence;

public class ProcessAssembler
{

    private static String lastError;

    public static class AssemblyException extends Exception
    {
    }

    public static ItemStack combine(ItemStack input1, ItemStack input2)
    {
        setLastError(null);
        if (input1 == null || input2 == null)
            return null;
        try
        {
            input1 = convertToSequence(input1);
            NBTTagList list1 = getProcessList(input1);
            NBTTagList list2 = getProcessList(input2);
            for (int i = 0; i < list2.tagCount(); i++)
                list1.appendTag(list2.getCompoundTagAt(i));
            return input1;
        }
        catch (AssemblyException e)
        {
            setLastError(e.getMessage());

            // TODO: DEBUG!
            e.printStackTrace();
            System.out.println("Could not combine magic sequences!");
            return null;
        }
    }

    public static ItemStack split(ItemStack input)
    {
        setLastError(null);
        if (input == null)
            return null;
        try
        {
            NBTTagList list1 = getProcessList(input);

            NBTTagCompound tag2 = new NBTTagCompound();
            NBTTagList list2 = new NBTTagList();
            tag2.setTag(ActivationSequence.NBT_PROCESSES, list2);
            list2.appendTag(list1.removeTag(list1.tagCount() - 1));
            
            // TODO: Convert input stack to process if tagCount == 1 as well

            MagicProcess process = MagicProcess.createFromNBT(list2.getCompoundTagAt(0));
            ItemStack output = new ItemStack(process.getItem());
            output.setTagCompound(tag2);
            return output;
        }
        catch (AssemblyException e)
        {
            setLastError(e.getMessage());

            // TODO: DEBUG!
            e.printStackTrace();
            System.out.println("Could not combine magic sequences!");
            return null;
        }
    }

    public static ItemStack convertToSequence(ItemStack stack) throws AssemblyException
    {
        if (stack.getItem() instanceof ItemMagicProcess)
        {
            ItemStack newStack = new ItemStack(MahoukaMod.item_magic_sequence);
            newStack.setTagCompound(getSequenceTag(stack));
            stack = newStack;
        }
        return stack;
    }

    public static ItemStack convertToProcess(ItemStack stack) throws AssemblyException
    {
        if (stack.getItem() instanceof ItemMagicSequence)
        {
            NBTTagCompound tag = getSequenceTag(stack);
            NBTTagList list = tag.getTagList(ActivationSequence.NBT_PROCESSES, 10);
            if (list.tagCount() == 1)
            {
                MagicProcess process = MagicProcess.createFromNBT(list.getCompoundTagAt(0));
                ItemStack newStack = new ItemStack(process.getItem());
                newStack.setTagCompound(tag);
                stack = newStack;
            }
        }
        return stack;
    }

    public static NBTTagCompound getSequenceTag(ItemStack stack) throws AssemblyException
    {
        if (!(stack.getItem() instanceof ItemMagicSequence))
            throw new AssemblyException();
        ItemMagicSequence item = (ItemMagicSequence) stack.getItem();
        NBTTagCompound tag = item.getStackData(stack);
        if (tag == null)
            throw new RuntimeException("Found magic sequence with empty data!");
        return tag;
    }

    public static NBTTagList getProcessList(ItemStack stack) throws AssemblyException
    {
        NBTTagList list = getSequenceTag(stack).getTagList(ActivationSequence.NBT_PROCESSES, 10);
        if (list == null)
            throw new RuntimeException("Found magic sequence with empty data!");
        return list;
    }

    public static String getLastError()
    {
        return lastError;
    }

    private static void setLastError(String lastError)
    {
        ProcessAssembler.lastError = lastError;
    }

}
