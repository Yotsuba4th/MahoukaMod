package de.yotsuba.mahouka.magic.cast;

import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.util.target.Target;

public class CastingProcess
{

    private ActivationSequence sequence;

    private EntityPlayer caster;

    private Target target;

    private UUID id;

    private int t;

    private int psion;

    private boolean active;

    public CastingProcess(EntityPlayer caster, ActivationSequence sequence, Target target, UUID id)
    {
        this.sequence = sequence;
        this.caster = caster;
        this.target = target;
        this.id = id;
        this.t = 0;

        psion = 10;
    }

    public void channelStart()
    {
        // TODO Auto-generated method stub
        active = true;
    }

    public void cancel()
    {
        // TODO Auto-generated method stub

    }

    public void tick()
    {
        t++;

        if (t < 50)
        {
            channelTick();
        }
        else if (t == 50)
        {
            channelEnd();
            cast();
        }
        else if (t < 80)
        {
            castTick();
        }
        else
        {
            castEnd();
            active = false;
        }
    }

    private void channelTick()
    {
        if (caster.worldObj.isRemote)
        {
            Vec3 point = target.toTargetPoint().getPoint();
            double x = point.xCoord + new Random().nextGaussian() * 0.5;
            double z = point.zCoord + new Random().nextGaussian() * 0.5;
            caster.worldObj.spawnParticle("witchMagic", x, point.yCoord + 1, z, 0, 0, 0);
        }
    }

    private void channelEnd()
    {
        // TODO Auto-generated method stub
        if (!caster.capabilities.isCreativeMode || true) // TODO: Remove test flag
        {
            PlayerData playerData = new PlayerData(caster);
            playerData.setPsion(playerData.getPsion() - psion);
            playerData.sendUpdate();
        }

        if (caster.worldObj.isRemote)
        {
            Vec3 point = target.toTargetPoint().getPoint();
            caster.worldObj.spawnParticle("heart", point.xCoord, point.yCoord + 1, point.zCoord, 0, 0, 0);
        }
    }

    private void cast()
    {
        if (caster.worldObj.isRemote)
        {
            MahoukaMod.proxy.clientCast(sequence.getProcesses().get(0), this, target);
        }
        else
        {
            sequence.getProcesses().get(0).cast(this, target);
        }
    }

    private void castTick()
    {
        // TODO Auto-generated method stub
        if (caster.worldObj.isRemote)
        {
            MahoukaMod.proxy.clientCastTick(sequence.getProcesses().get(0), this, target);
        }
        else
        {
            sequence.getProcesses().get(0).castTick(this, target);
        }
    }

    private void castEnd()
    {
        // TODO Auto-generated method stub
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
        return active;
    }

}
