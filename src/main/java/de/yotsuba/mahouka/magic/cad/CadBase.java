package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.Target;

public class CadBase
{

    private String id;

    private ActivationSequence[] activationSequences = new ActivationSequence[1];

    private byte selectedSequence;

    private boolean channeling;

    private boolean casting;

    private Target currentTarget;

    private EntityPlayer caster;

    private PlayerData playerData;

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
        setCaster(player);
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
        stack.setItemDamage(playerData.getPsion() * 100 / playerData.getMaxPsion());
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
        if (getSelectedSequence() == null)
        {
            // TODO: Error sound / message
            caster.addChatMessage(new ChatComponentText("No sequence selected!"));
            return;
        }

        if (!caster.worldObj.isRemote && playerData.getPsion() < 10)
        {
            // TODO: Error sound / message
            caster.addChatMessage(new ChatComponentText("Not enough psion!"));
            return;
        }

        channeling = true;

        // TODO: Remove skipping of channeling
        channelComplete();
    }

    public void channelComplete()
    {
        if (!channeling)
            return;
        channeling = false;
        casting = true;

        playerData.setPsion(playerData.getPsion() - 10);

        getSelectedSequence().getProcesses().get(0).cast(this, currentTarget);
        MahoukaMod.proxy.clientCast(getSelectedSequence().getProcesses().get(0), this, currentTarget);

        getSelectedSequence().getProcesses().get(0).castTick(this, currentTarget);
        MahoukaMod.proxy.clientCastTick(getSelectedSequence().getProcesses().get(0), this, currentTarget);
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

    public int getSelectedSequenceIndex()
    {
        return selectedSequence;
    }

    public void setSelectedSequenceIndex(byte index)
    {
        if (channeling || casting)
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

    public EntityPlayer getCaster()
    {
        return caster;
    }

    private void setCaster(EntityPlayer player)
    {
        caster = player;
        playerData = new PlayerData(caster);
    }

    public boolean isChanneling()
    {
        return channeling;
    }

    public boolean isCasting()
    {
        return casting;
    }

}
