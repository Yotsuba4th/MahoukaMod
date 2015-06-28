package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.CastingManager;
import de.yotsuba.mahouka.magic.CastingProcess;
import de.yotsuba.mahouka.magic.Target;
import de.yotsuba.mahouka.util.Utils;

public class CadBase
{

    private UUID id;

    private ActivationSequence[] activationSequences = new ActivationSequence[1];

    private byte selectedSequence;

    /* ------------------------------------------------------------ */

    public CadBase()
    {
        id = UUID.randomUUID();
    }

    /* ------------------------------------------------------------ */

    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setBoolean("changed", true);
        tag.setString("id", id.toString());
        tag.setByte("sel", selectedSequence);

        // Write sequences
        NBTTagList tagSequences = new NBTTagList();
        tag.setTag("seq", tagSequences);
        for (int i = 0; i < activationSequences.length; i++)
        {
            if (activationSequences[i] == null)
                continue;
            NBTTagCompound tagSequence = activationSequences[i].writeToNBT();
            tagSequence.setByte("idx", (byte) i);
            tagSequences.appendTag(tagSequence);
        }
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        id = UUID.fromString(tag.getString("id"));
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

    /* ------------------------------------------------------------ */

    public void rightClick(ItemStack stack, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
            return;
        if (CastingManager.isCasting(id))
        {
            CastingManager.cancelCast(id);
        }
        else
        {
            if (getSelectedSequence() == null)
            {
                // TODO: Error sound / message
                player.addChatMessage(new ChatComponentText("No sequence selected!"));
                return;
            }

            PlayerData playerData = new PlayerData(player);
            if (playerData.getPsion() < 10)
            {
                // TODO: Error sound / message
                player.addChatMessage(new ChatComponentText("Not enough psion!"));
                return;
            }

            Target target = selectTarget(player);
            if (target == null)
            {
                // TODO: Error sound / message
                player.addChatMessage(new ChatComponentText("No target selected!"));
                return;
            }

            CastingProcess cast = new CastingProcess(player, getSelectedSequence(), target, id);
            CastingManager.startChanneling(cast);
        }
        updateItemStack(stack, player);
    }

    public void updateItemStack(ItemStack stack, EntityPlayer player)
    {
        PlayerData playerData = new PlayerData(player);
        stack.setItemDamage(stack.getItem().getMaxDamage() - 1 - playerData.getPsion() * stack.getItem().getMaxDamage() / playerData.getMaxPsion());
        writeToNBT(stack.getTagCompound());
    }

    public Target selectTarget(EntityPlayer player)
    {
        return new Target.TargetPoint(Utils.getLookingAtPoint(player, 100));
    }

    /* ------------------------------------------------------------ */

    public UUID getId()
    {
        return id;
    }

    public ActivationSequence[] getActivationSequences()
    {
        return activationSequences;
    }

    public int getSelectedSequenceIndex()
    {
        return selectedSequence;
    }

    public void setSelectedSequenceIndex(byte index)
    {
        if (CastingManager.isCasting(id))
            return;
        if (index < 0)
            selectedSequence = (byte) (activationSequences.length - 1);
        else if (index >= activationSequences.length)
            selectedSequence = 0;
        else
            selectedSequence = index;
    }

    public ActivationSequence getSelectedSequence()
    {
        return activationSequences[selectedSequence];
    }

}
