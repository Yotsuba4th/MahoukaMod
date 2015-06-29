package de.yotsuba.mahouka.magic.cast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.util.target.Target;

public abstract class CastingManager
{

    protected Map<UUID, CastingProcess> casts = new HashMap<UUID, CastingProcess>();

    /* ------------------------------------------------------------ */

    public CastingManager()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    /* ------------------------------------------------------------ */

    public boolean isCasting(UUID cadId)
    {
        CastingProcess cast = casts.get(cadId);
        return cast != null && cast.isActive();
    }

    public CastingProcess constructCastingProcess(EntityPlayer caster, ActivationSequence sequence, Target target, UUID id)
    {
        // TODO: Check and construct magic sequence?

        CastingProcess cast = new CastingProcess(caster, sequence, target, id, 10, 50);
        return cast;
    }

}