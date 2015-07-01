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

    public boolean isCasting(UUID id)
    {
        CastingProcess cast = casts.get(id);
        return cast != null && cast.isActive();
    }

    public CastingProcess constructCastingProcess(EntityPlayer caster, ActivationSequence sequence, Target target, UUID id)
    {
        // TODO: Check if magic sequence is valid for target!!

        int channelingDuration = sequence.getChannelingDuration();
        int psionCost = sequence.getPsionCost();
        CastingProcess cast = new CastingProcess(caster, sequence, target, id, psionCost, channelingDuration);
        return cast;
    }

}