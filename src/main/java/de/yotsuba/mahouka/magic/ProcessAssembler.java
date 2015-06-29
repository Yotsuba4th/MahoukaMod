package de.yotsuba.mahouka.magic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.item.ItemMagicSequence;

public class ProcessAssembler
{

    public static class AssemblyException extends Exception
    {
    }

    public static ItemStack combine(ItemStack input1, ItemStack input2) throws AssemblyException
    {
        input1 = convertToSequence(input1);
        NBTTagList list1 = getProcessList(input1);
        NBTTagList list2 = getProcessList(input2);
        for (int i = 0; i < list2.tagCount(); i++)
            list1.appendTag(list2.getCompoundTagAt(i));
        return input1;
    }

    public static ItemStack split(ItemStack input) throws AssemblyException
    {
        NBTTagList list1 = getProcessList(input);

        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagList list2 = new NBTTagList();
        tag2.setTag(ActivationSequence.NBT_PROCESSES, list2);
        list2.appendTag(list1.removeTag(list1.tagCount() - 1));

        ItemStack output = new ItemStack(MahoukaMod.item_magic_sequence);
        output.setTagCompound(tag2);
        return output;
    }

    private static ItemStack convertToSequence(ItemStack stack) throws AssemblyException
    {
        if (stack.getItem() instanceof ItemMagicProcess)
        {
            ItemStack newStack = new ItemStack(MahoukaMod.item_magic_sequence);
            newStack.setTagCompound(getSequenceTag(stack));
        }
        return stack;
    }

    public static NBTTagCompound getSequenceTag(ItemStack input1) throws AssemblyException
    {
        if (!(input1.getItem() instanceof ItemMagicSequence))
            throw new AssemblyException();
        ItemMagicSequence item = (ItemMagicSequence) input1.getItem();
        NBTTagCompound tag = item.getStackData(input1);
        if (tag == null)
            throw new RuntimeException("Found magic sequence with empty data!");
        return tag;
    }

    public static NBTTagList getProcessList(ItemStack input1) throws AssemblyException
    {
        NBTTagList list = getSequenceTag(input1).getTagList(ActivationSequence.NBT_PROCESSES, 10);
        if (list == null)
            throw new RuntimeException("Found magic sequence with empty data!");
        return list;
    }

}