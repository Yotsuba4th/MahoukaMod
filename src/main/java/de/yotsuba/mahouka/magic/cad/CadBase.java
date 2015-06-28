package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.Target;

public class CadBase
{

    private String id;

    private ActivationSequence[] activationSequences = new ActivationSequence[1];

    private byte selectedSequence;

    private boolean channeling;

    private Target currentTarget;

    /* ------------------------------------------------------------ */

    public CadBase()
    {
        id = UUID.randomUUID().toString();
    }

    /* ------------------------------------------------------------ */

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

    /* ------------------------------------------------------------ */

    public void rightClick(ItemStack stack, EntityPlayer player)
    {
        if (stack.getItemDamage() >= stack.getMaxDamage())
        {
            // TODO: Play error sound
            return;
        }

        // if (!player.capabilities.isCreativeMode)

        if (channeling)
        {
            cancelChanneling();
        }
        else
        {
            selectTarget(player);
            if (currentTarget != null)
            {
                startChanneling();
            }
        }
        updateItemStack(stack, player);
    }

    private void updateItemStack(ItemStack stack, EntityPlayer player)
    {
        PlayerData data = new PlayerData(player);
        stack.setItemDamage(data.getPsion() * 100 / data.getMaxPsion());
        writeToNBT(stack.getTagCompound());
    }

    public void selectTarget(EntityPlayer player)
    {
        currentTarget = new Target.TargetPoint(getLookingAtPoint(player, 100));
    }

    public Vec3 getLookingAtPoint(EntityPlayer player, double maxDistance)
    {
        Vec3 lookAt = player.getLook(1);
        Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY + (player.getEyeHeight() - player.getDefaultEyeHeight()), player.posZ);
        Vec3 start = playerPos.addVector(0, player.getEyeHeight(), 0);
        Vec3 end = start.addVector(lookAt.xCoord * maxDistance, lookAt.yCoord * maxDistance, lookAt.zCoord * maxDistance);
        MovingObjectPosition result = player.worldObj.rayTraceBlocks(start, end, false);
        if (result != null)
            return result.hitVec;
        return end;
    }

    /* ------------------------------------------------------------ */

    public void startChanneling()
    {
    }

    public void channelComplete()
    {
    }

    public void cancelChanneling()
    {
    }

    public void stopCasting()
    {
    }

    /* ------------------------------------------------------------ */

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
