package de.yotsuba.mahouka.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import de.yotsuba.mahouka.util.MathUtils;
import de.yotsuba.mahouka.util.Utils;

public class PlayerMotionTracker
{

    protected static class PlayerMotionData
    {

        public double lastX;

        public double lastY;

        public double lastZ;

        public double motionX;

        public double motionY;

        public double motionZ;

        public PlayerMotionData(EntityPlayer player)
        {
            lastX = player.posX;
            lastY = player.posY;
            lastZ = player.posZ;
        }

        public Vec3 getMotion()
        {
            return Vec3.createVectorHelper(motionX, motionY, motionZ);
        }

        public void update(EntityPlayerMP player)
        {
            motionX = motionX * 0.75 + (player.posX - lastX) * 0.25;
            motionY = motionY * 0.75 + (player.posY - lastY) * 0.25;
            motionZ = motionZ * 0.75 + (player.posZ - lastZ) * 0.25;
            lastX = player.posX;
            lastY = player.posY;
            lastZ = player.posZ;
            // System.out.println(motionX);
        }

    }

    public static Map<UUID, PlayerMotionData> motionData = new HashMap<UUID, PlayerMotionTracker.PlayerMotionData>();

    public PlayerMotionTracker()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void playerLoggedOutEvent(PlayerLoggedOutEvent event)
    {
        motionData.remove(event.player.getPersistentID());
    }

    @SubscribeEvent
    public void serverTickEvent(ServerTickEvent event)
    {
        if (event.phase != Phase.START)
            return;
        for (EntityPlayerMP player : Utils.getPlayerList())
            getMotionData(player).update(player);
    }

    public static PlayerMotionData getMotionData(EntityPlayer player)
    {
        PlayerMotionData data = motionData.get(player.getPersistentID());
        if (data == null)
        {
            data = new PlayerMotionData(player);
            motionData.put(player.getPersistentID(), data);
        }
        return data;
    }

    public static Vec3 getPlayerMotion(EntityPlayer player)
    {
        return getMotionData(player).getMotion();
    }

    public static Vec3 getEntityMotion(Entity entity)
    {
        return (entity instanceof EntityPlayer) ? getPlayerMotion((EntityPlayer) entity) : MathUtils.getEntityMotion(entity);
    }

}
