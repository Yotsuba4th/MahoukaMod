package de.yotsuba.mahouka.magic;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.util.target.Target;

public class CastingProcess
{

    private ActivationSequence sequence;

    private EntityPlayer caster;

    private Target target;

    private UUID id;

    public CastingProcess(EntityPlayer caster, ActivationSequence sequence, Target target, UUID id)
    {
        this.sequence = sequence;
        this.caster = caster;
        this.target = target;
        this.id = id;
    }

    public void channelComplete()
    {
        if (!caster.capabilities.isCreativeMode || true) // TODO: Remove test flag
        {
            PlayerData playerData = new PlayerData(caster);
            playerData.setPsion(playerData.getPsion() - 10);
            playerData.sendUpdate();
        }

        if (caster.worldObj.isRemote)
        {
            MahoukaMod.proxy.clientCast(sequence.getProcesses().get(0), this, target);
            MahoukaMod.proxy.clientCastTick(sequence.getProcesses().get(0), this, target);
        }
        else
        {
            sequence.getProcesses().get(0).cast(this, target);
            sequence.getProcesses().get(0).castTick(this, target);
        }
    }

    public UUID getId()
    {
        return id;
    }

    public ActivationSequence getSequence()
    {
        return sequence;
    }

    public EntityPlayer getCaster()
    {
        return caster;
    }

    public Target getTarget()
    {
        return target;
    }

    public boolean isActive()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
