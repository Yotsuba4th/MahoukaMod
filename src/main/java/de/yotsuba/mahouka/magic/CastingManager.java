package de.yotsuba.mahouka.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.yotsuba.mahouka.network.C1StartChanneling;

public class CastingManager
{

    public static Map<UUID, CastingProcess> casts = new HashMap<UUID, CastingProcess>();

    /* ------------------------------------------------------------ */

    public static void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        casts.clear();
    }

    /* ------------------------------------------------------------ */

    public static boolean isCasting(UUID cadId)
    {
        CastingProcess cast = casts.get(cadId);
        return cast != null && !cast.isActive();
    }

    public static boolean startChanneling(CastingProcess cast)
    {
        if (isCasting(cast.getId()))
        {
            // TODO: Error sound / message
            cast.getCaster().addChatMessage(new ChatComponentText("Another magic is still active!"));
            return false;
        }
        C1StartChanneling.send(cast);
        cast.channelComplete();
        return true;
    }

    public static void startChannelingClient(CastingProcess cast)
    {
        cast.channelComplete();
    }

    public static void cancelCast(UUID cadId)
    {
        if (!isCasting(cadId))
            return;

        // TODO Auto-generated method stub
    }

}
