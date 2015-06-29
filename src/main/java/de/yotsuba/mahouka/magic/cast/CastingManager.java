package de.yotsuba.mahouka.magic.cast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;

public abstract class CastingManager
{

    protected Map<UUID, CastingProcess> casts = new HashMap<UUID, CastingProcess>();

    public CastingManager()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    public boolean isCasting(UUID cadId)
    {
        CastingProcess cast = casts.get(cadId);
        return cast != null && cast.isActive();
    }

    public void tick()
    {
        for (Iterator<CastingProcess> it = casts.values().iterator(); it.hasNext();)
        {
            CastingProcess cast = it.next();
            cast.tick();
            if (!cast.isActive())
                it.remove();
        }
    }

}