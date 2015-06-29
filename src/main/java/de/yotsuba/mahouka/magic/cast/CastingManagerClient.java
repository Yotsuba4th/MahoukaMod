package de.yotsuba.mahouka.magic.cast;

import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class CastingManagerClient extends CastingManager
{

    public CastingManagerClient()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void serverStoppedEvent(PlayerLoggedOutEvent event)
    {
        casts.clear();
    }

    @SubscribeEvent
    public void clientTickEvent(ClientTickEvent event)
    {
        if (event.phase == Phase.START)
            tick();
    }

    /* ------------------------------------------------------------ */

    public void startChanneling(CastingProcess cast)
    {
        casts.put(cast.getId(), cast);
    }

    public void cancelCast(UUID id)
    {
        CastingProcess cast = casts.get(id);
        if (cast != null)
        {
            cast.cancel();
            casts.remove(id);
        }
    }

}
