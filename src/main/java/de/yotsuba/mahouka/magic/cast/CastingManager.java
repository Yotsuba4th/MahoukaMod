package de.yotsuba.mahouka.magic.cast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicProcess;
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

    public static int getChannelingDuration(ActivationSequence sequence)
    {
        int t = 0;
        for (MagicProcess process : sequence.getProcesses())
            t += process.getChannelingDuration();
        return t;
    }

    public static int getPsionCost(ActivationSequence sequence)
    {
        int psionCost = 0;
        for (MagicProcess process : sequence.getProcesses())
            psionCost += process.getPsionCost();
        return psionCost;
    }

    public CastingProcess constructCastingProcess(EntityPlayer caster, ActivationSequence sequence, Target target, UUID id)
    {
        // TODO: Check if magic sequence is valid for target!!

        int channelingDuration = getChannelingDuration(sequence);
        int psionCost = getPsionCost(sequence);
        CastingProcess cast = new CastingProcess(caster, sequence, target, id, psionCost, channelingDuration);
        return cast;
    }

}