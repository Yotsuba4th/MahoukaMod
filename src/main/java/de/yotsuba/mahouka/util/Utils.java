package de.yotsuba.mahouka.util;

import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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

    public static Vec3 getLookingAtPoint(EntityPlayer player, double maxDistance)
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

    public static UUID uuidFromBytes(ByteBuf buf)
    {
        long hi = buf.readLong();
        long lo = buf.readLong();
        return new UUID(hi, lo);
    }

    public static void uuidToBytes(ByteBuf buf, UUID uuid)
    {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public static void writeVec3(ByteBuf buf, Vec3 vec)
    {
        buf.writeDouble(vec.xCoord);
        buf.writeDouble(vec.yCoord);
        buf.writeDouble(vec.zCoord);
    }

}
