package de.yotsuba.mahouka.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.util.Utils;

public class PlayerData
{

    public static final String TAG_NAME = MahoukaMod.MODID;

    private EntityPlayer player;

    private NBTTagCompound tag;

    /* ------------------------------------------------------------ */

    public PlayerData(EntityPlayer player)
    {
        this.player = player;

        NBTTagCompound playerTag = Utils.getPlayerPersistedTag(player);
        tag = playerTag.getCompoundTag(TAG_NAME);
        if (!playerTag.hasKey(TAG_NAME))
        {
            // TODO (3) Proper initialization of player data
            setMaxPsion(1000);
            setPsion(1000);
        }
        playerTag.setTag(TAG_NAME, tag);

        sendUpdate();
    }

    public NBTTagCompound getTag()
    {
        return tag;
    }

    public void sendUpdate()
    {
        if (player instanceof EntityPlayerMP)
            C0PlayerData.send((EntityPlayerMP) player);
    }

    /* ------------------------------------------------------------ */

    public int getPsion()
    {
        return tag.getInteger("psion");
    }

    public void setPsion(int value)
    {
        tag.setInteger("psion", Math.min(getMaxPsion(), value));
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
