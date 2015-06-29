package de.yotsuba.mahouka.magic.cast;

import java.util.Iterator;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import de.yotsuba.mahouka.util.target.Target;

public class CastingManagerClient extends CastingManager
{

    @SubscribeEvent
    public void clientDisconnectionFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
    {
        casts.clear();
    }

    @SubscribeEvent
    public void clientTickEvent(ClientTickEvent event)
    {
        if (event.phase != Phase.START)
            return;

        for (Iterator<CastingProcess> it = casts.values().iterator(); it.hasNext();)
        {
            CastingProcess cast = it.next();
            cast.clientTick();
            if (!cast.isActive())
                it.remove();
        }
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

    public void castUpdate(UUID id, int process, Target target)
    {
        CastingProcess cast = casts.get(id);
        if (cast != null)
            cast.clientUpdate(process, target);
    }

}
