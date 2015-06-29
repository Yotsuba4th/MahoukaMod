package de.yotsuba.mahouka.magic.cast;

import java.util.UUID;

import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import de.yotsuba.mahouka.network.C2StartChanneling;
import de.yotsuba.mahouka.network.C3CancelCast;

public class CastingManagerServer extends CastingManager
{

    public void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        casts.clear();
    }

    @SubscribeEvent
    public void serverTickEvent(ServerTickEvent event)
    {
        if (event.phase == Phase.START)
            tick();
    }

    /* ------------------------------------------------------------ */

    public boolean startChanneling(CastingProcess cast)
    {
        if (isCasting(cast.getId()))
        {
            // TODO: Error sound / message
            cast.getCaster().addChatMessage(new ChatComponentText("Another magic is still active!"));
            return false;
        }
        C2StartChanneling.send(cast);
        casts.put(cast.getId(), cast);
        return true;
    }

    public void cancelCast(UUID cadId)
    {
        CastingProcess cast = casts.get(cadId);
        if (cast == null)
            return;
        cast.cancel();
        C3CancelCast.send(cast);
    }

}
