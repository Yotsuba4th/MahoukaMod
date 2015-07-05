package de.yotsuba.mahouka.core;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.cast.CastingManager;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.util.Utils;

public class MahoukaEventHandler
{

    public MahoukaEventHandler()
    {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerLoggedInEvent(PlayerLoggedInEvent event)
    {
        C0PlayerData.send((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void clientDisconnectionFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
    {
        CastingManager.clientDisconnectEvent();
    }

    @SubscribeEvent
    public void serverTickEvent(ServerTickEvent event)
    {
        if (event.phase != Phase.START)
            return;

        if (MinecraftServer.getServer().getEntityWorld().getWorldInfo().getWorldTotalTime() % 20 == 0)
        {
            for (EntityPlayerMP player : Utils.getPlayerList())
            {
                PlayerData data = new PlayerData(player);
                data.setPsion(data.getPsion() + 10);
                data.sendUpdate();
            }
        }
    }

    @SubscribeEvent
    public void entityInteractEvent(EntityInteractEvent event)
    {
        if (event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemCad)
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void playerInteractEvent(PlayerInteractEvent event)
    {
        // if (event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemCad)
        // {
        // event.useBlock = Result.DENY;
        // }
    }

}
