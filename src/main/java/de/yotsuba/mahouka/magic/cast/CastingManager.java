package de.yotsuba.mahouka.magic.cast;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class CastingManager
{

    protected static Map<UUID, CastingProcess> serverCasts = new ConcurrentHashMap<UUID, CastingProcess>();

    protected static Map<UUID, CastingProcess> clientCasts = new ConcurrentHashMap<UUID, CastingProcess>();

    /* ------------------------------------------------------------ */

    public CastingManager()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    public static void clientDisconnectEvent()
    {
        clientCasts.clear();
    }

    public static void serverStoppedEvent()
    {
        serverCasts.clear();
    }

    @SubscribeEvent
    public void clientTickEvent(ClientTickEvent event)
    {
        if (event.phase != Phase.START)
            return;
        for (Iterator<CastingProcess> it = clientCasts.values().iterator(); it.hasNext();)
        {
            CastingProcess cast = it.next();
            cast.clientTick();
            if (!cast.isActive())
                it.remove();
        }
    }

    @SubscribeEvent
    public void serverTickEvent(ServerTickEvent event)
    {
        if (event.phase != Phase.START)
            return;
        for (Iterator<Entry<UUID, CastingProcess>> it = serverCasts.entrySet().iterator(); it.hasNext();)
        {
            CastingProcess cast = it.next().getValue();
            cast.serverTick();
            if (!cast.isActive())
                it.remove();
        }
    }

    /* ------------------------------------------------------------ */

    public static CastingProcess getServerCast(UUID id)
    {
        return serverCasts.get(id);
    }

    public static CastingProcess getClientCast(UUID id)
    {
        return clientCasts.get(id);
    }

    public static boolean isServerCasting(UUID id)
    {
        CastingProcess cast = serverCasts.get(id);
        return cast != null && cast.isActive();
    }

    public static boolean isClientCasting(UUID id)
    {
        CastingProcess cast = clientCasts.get(id);
        return cast != null && cast.isActive();
    }

}
