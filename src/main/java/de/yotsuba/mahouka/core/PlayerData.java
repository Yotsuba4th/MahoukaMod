package de.yotsuba.mahouka.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerData
{

    private NBTTagCompound tag;

    /* ------------------------------------------------------------ */

    public PlayerData(EntityPlayer player)
    {
        NBTTagCompound playerTag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, playerTag);

        tag = playerTag.getCompoundTag("mahouka");
        if (!playerTag.hasKey("mahouka"))
        {
            // TODO Proper initialization
            setMaxPsion(1000);
            setPsion(1000);
        }
        playerTag.setTag("mahouka", tag);
    }

    public NBTTagCompound getTag()
    {
        return tag;
    }

    /* ------------------------------------------------------------ */

    public int getPsion()
    {
        return tag.getInteger("psion");
    }

    public void setPsion(int value)
    {
        tag.setInteger("psion", value);
    }

    public int getMaxPsion()
    {
        return tag.getInteger("max_psion");
    }

    public void setMaxPsion(int value)
    {
        tag.setInteger("max_psion", value);
    }

}
