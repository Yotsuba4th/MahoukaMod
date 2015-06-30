package de.yotsuba.mahouka.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Utils
{

    public static final int TICKS_PER_SECOND = 20;

    public static int millisecondsToTicks(int milliseconds)
    {
        return secondsToTicks(milliseconds) / 1000;
    }

    public static int secondsToTicks(int seconds)
    {
        return TICKS_PER_SECOND * seconds;
    }

    public static NBTTagCompound getPlayerPersistedTag(EntityPlayer player)
    {
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
        return tag;
    }

    @SuppressWarnings("unchecked")
    public static List<EntityPlayerMP> getPlayerList()
    {
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    }

    public static EntityPlayer getPlayerByUuid(UUID uuid)
    {
        for (EntityPlayer player : getPlayerList())
            if (player.getPersistentID().equals(uuid))
                return player;
        return null;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public static List<EntityPlayer> getClientPlayerList()
    {
        return Minecraft.getMinecraft().theWorld.playerEntities;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayerByUuid(UUID uuid)
    {
        for (EntityPlayer player : getClientPlayerList())
            if (player.getPersistentID().equals(uuid))
                return player;
        return null;
    }

    public static List<Slot> getPlayerContainerSlots(InventoryPlayer playerInventory)
    {
        List<Slot> slots = new ArrayList<Slot>();
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                slots.add(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            slots.add(new Slot(playerInventory, i, 8 + i * 18, 142));
        return slots;
    }

}
