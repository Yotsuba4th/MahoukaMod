package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicSequence;
import de.yotsuba.mahouka.magic.Target;

public class CadBase
{

    private String id;

    private ActivationSequence[] activationSequences = new ActivationSequence[1];

    private byte selectedSequence;

    public CadBase()
    {
        id = UUID.randomUUID().toString();
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setBoolean("changed", true);
        tag.setString("id", id);
        tag.setByte("sel", selectedSequence);

        // Write sequences
        NBTTagList tagSequences = new NBTTagList();
        tag.setTag("seq", tagSequences);
        for (int i = 0; i < activationSequences.length; i++)
        {
            if (activationSequences[i] == null)
                continue;
            NBTTagCompound tagSequence = new NBTTagCompound();
            activationSequences[i].writeToNBT(tagSequence);
            tagSequence.setByte("idx", (byte) i);
            tagSequences.appendTag(tagSequence);
        }
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        id = tag.getString("id");
        selectedSequence = tag.getByte("sel");

        // Read sequences
        NBTTagList tagSequences = tag.getTagList("seq", 10);
        for (int i = 0; i < activationSequences.length; i++)
            activationSequences[i] = null;
        for (int i = 0; i < tagSequences.tagCount(); i++)
        {
            NBTTagCompound tagSequence = tagSequences.getCompoundTagAt(i);
            activationSequences[tagSequence.getByte("idx")] = new ActivationSequence(tagSequence);
        }
    }

    public void rightClick(ItemStack stack, EntityPlayer player)
    {
        if (stack.getItemDamage() >= stack.getMaxDamage())
        {
            // TODO: Play error sound
            return;
        }

        // TODO: Use cad
        stack.setItemDamage(50);

        // if (!player.capabilities.isCreativeMode)
    }

    public MagicSequence startChanneling(Target target, ActivationSequence sequence)
    {
        return null;
    }

    public void channelComplete(MagicSequence cast, ActivationSequence sequence)
    {
    }

    public String getId()
    {
        return id;
    }

    public ActivationSequence[] getActivationSequences()
    {
        return activationSequences;
    }

    public int getSelectedSequence()
    {
        return selectedSequence;
    }

    public void setSelectedSequence(byte index)
    {
        if (index < 0)
            selectedSequence = (byte) (activationSequences.length - 1);
        else if (index >= activationSequences.length)
            selectedSequence = 0;
        else
            selectedSequence = index;
    }

}
