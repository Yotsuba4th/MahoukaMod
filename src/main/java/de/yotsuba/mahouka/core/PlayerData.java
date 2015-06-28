package de.yotsuba.mahouka.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerData
{

    private NBTTagCompound tag;

    /* ------------------------------------------------------------ */

    public PlayerData(EntityPlayer player)
    {
        NBTTagCompound playerTag = ((NBTTagCompound) player.getEntityData().getTag(EntityPlayer.PERSISTED_NBT_TAG));
        tag = playerTag.getCompoundTag("mahouka");
        if (tag == null)
        {
            tag = new NBTTagCompound();
            playerTag.setTag("mahouka", tag);

            // TODO
            setMaxPsion(1000);
            setPsion(1000);
        }
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
