package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.CastingManager;
import de.yotsuba.mahouka.network.S2StartChanneling;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetPoint;

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
        if (!player.worldObj.isRemote)
            return;
        if (CastingManager.isCasting(id))
        {
            // CastingManager.cancelCast(id);
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

            S2StartChanneling.send(player, id, target);

            // CastingProcess cast = new CastingProcess(player, getSelectedSequence(), target, id);
            // CastingManager.startChanneling(cast);
        }
        updateItemStack(stack, player);
    }

    public void updateItemStack(ItemStack stack, EntityPlayer player)
    {
        PlayerData playerData = new PlayerData(player);
        stack.setItemDamage(stack.getItem().getMaxDamage() - 1 - playerData.getPsion() * stack.getItem().getMaxDamage() / playerData.getMaxPsion());
        writeToNBT(stack.getTagCompound());
    }

    @SideOnly(Side.CLIENT)
    public Target selectTarget(EntityPlayer player)
    {
        MovingObjectPosition result = Minecraft.getMinecraft().objectMouseOver;
        if (result.typeOfHit == MovingObjectType.ENTITY)
            return new TargetEntity(result.entityHit, false, false);
        if (result.typeOfHit == MovingObjectType.BLOCK)
            return new TargetBlock(player.worldObj, result.blockX, result.blockY, result.blockZ, result.hitVec);

        double maxDistance = 32;
        Vec3 lookAt = player.getLook(1);
        Vec3 playerPos = Vec3.createVectorHelper(player.posX, player.posY + (player.getEyeHeight() - player.getDefaultEyeHeight()), player.posZ);
        Vec3 start = playerPos.addVector(0, player.getEyeHeight(), 0);
        Vec3 end = start.addVector(lookAt.xCoord * maxDistance, lookAt.yCoord * maxDistance, lookAt.zCoord * maxDistance);
        // start = start.addVector(lookAt.xCoord * 1, lookAt.yCoord * 1, lookAt.zCoord * 1);
        result = player.worldObj.func_147447_a(start, end, false, false, false);
        if (result == null || result.typeOfHit == MovingObjectType.MISS)
            return new TargetPoint(end);

        if (result.entityHit != null)
            return new TargetEntity(result.entityHit, false, false);

        return new TargetBlock(player.worldObj, result.blockX, result.blockY, result.blockZ, result.hitVec);
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
