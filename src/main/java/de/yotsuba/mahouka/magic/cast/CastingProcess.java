package de.yotsuba.mahouka.magic.cast;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.network.C5CastUpdate;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public class CastingProcess
{

    private ActivationSequence sequence;

    private EntityPlayer caster;

    private Target target;

    private UUID id;

    private int channelTime;

    private int psion;

    /* ------------------------------------------------------------ */

    private int t;

    private int ct;

    private boolean active = true;

    private int processIndex = -1;

    private Target currentTarget;

    /* ------------------------------------------------------------ */

    public CastingProcess(EntityPlayer caster, ActivationSequence sequence, Target target, UUID id, int psion, int channelTime)
    {
        this.id = id;
        this.sequence = sequence;
        this.caster = caster;
        this.target = target;
        this.psion = psion;
        this.channelTime = channelTime;
        t = 0;
        currentTarget = target;
    }

    /* ------------------------------------------------------------ */

    public static CastingProcess fromBytes(World world, ByteBuf buf)
    {
        UUID id = BufUtils.uuidFromBytes(buf);
        EntityPlayer caster = Utils.getClientPlayerByUuid(BufUtils.uuidFromBytes(buf));
        Target target = Target.fromBytes(world, buf);
        ActivationSequence sequence = new ActivationSequence(ByteBufUtils.readTag(buf));
        int psion = buf.readInt();
        int channelTime = buf.readInt();
        if (id == null || caster == null || target == null || sequence == null)
            return null;
        return new CastingProcess(caster, sequence, target, id, psion, channelTime);
    }

    public void toBytes(ByteBuf buf)
    {
        BufUtils.uuidToBytes(buf, id);
        BufUtils.uuidToBytes(buf, caster.getPersistentID());
        target.toBytes(buf);
        ByteBufUtils.writeTag(buf, sequence.writeToNBT());
        buf.writeInt(psion);
        buf.writeInt(channelTime);
    }

    /* ------------------------------------------------------------ */

    public boolean cancel()
    {
        // TODO Cast cancelling

        active = false;

        return true;
    }

    public void clientUpdate(int newProcess, Target target)
    {
        if (processIndex < 0)
        {
            channelEndClient();
            castStartClient();
        }
        else
        {
            MagicProcess process = sequence.getProcesses().get(processIndex);
            process.castEndClient(this, currentTarget);
        }

        processIndex = newProcess;
        currentTarget = target;
        if (processIndex < sequence.getProcesses().size())
        {
            MagicProcess process = sequence.getProcesses().get(processIndex);
            process.castStartClient(this, currentTarget);
        }
        else
        {
            castEndClient();
        }
    }

    /* ------------------------------------------------------------ */

    public void serverTick()
    {
        if (t == 0)
        {
            channelStart();
        }
        if (t < channelTime)
        {
            channelTick();
        }
        else if (t == channelTime)
        {
            channelEnd();
            castStart();
            castTick();
        }
        else
        {
            castTick();
        }
        t++;
    }

    public void clientTick()
    {
        if (processIndex < 0)
        {
            if (t == 0)
                channelStartClient();
            channelTickClient();
        }
        else if (processIndex < sequence.getProcesses().size())
        {
            castTickClient();
        }
        t++;
    }

    /* ------------------------------------------------------------ */

    private void channelStart()
    {
        /* do nothing */
    }

    private void channelStartClient()
    {
        // TODO: DEBUG
        // Vec3 point = currentTarget.getCurrentPoint();
        // EntityFxRune entityFx = new EntityFxRune(caster.worldObj, point.xCoord, point.yCoord + 0.01, point.zCoord, 0, 0, 0);
        // entityFx.setFlat(true);
        // entityFx.setRadius(2);
        // Minecraft.getMinecraft().effectRenderer.addEffect(entityFx);
    }

    /* ------------------------------------------------------------ */

    private void channelTick()
    {
        /* do nothing */
    }

    private void channelTickClient()
    {
        // Vec3 point = currentTarget.getCurrentPoint();
        // double x = point.xCoord + new Random().nextGaussian() * 0.5;
        // double z = point.zCoord + new Random().nextGaussian() * 0.5;
        // caster.worldObj.spawnParticle("instantSpell", x, point.yCoord + 1, z, 0, 0, 0);
    }

    /* ------------------------------------------------------------ */

    private void channelEnd()
    {
        if (!caster.capabilities.isCreativeMode)
        {
            PlayerData playerData = new PlayerData(caster);
            playerData.setPsion(playerData.getPsion() - psion);
            playerData.sendUpdate();
        }
    }

    private void channelEndClient()
    {
        // TODO: DEBUG
        Vec3 point = currentTarget.getCurrentPoint();
        caster.worldObj.spawnParticle("heart", point.xCoord, point.yCoord + 1, point.zCoord, 0, 0, 0);
    }

    /* ------------------------------------------------------------ */

    private void castStart()
    {
        /* do nothing */
    }

    private void castStartClient()
    {
        /* do nothing */
    }

    /* ------------------------------------------------------------ */

    private void castTick()
    {
        if (processIndex >= sequence.getProcesses().size())
            return;

        MagicProcess process = (processIndex < 0) ? null : sequence.getProcesses().get(processIndex);
        if (process != null)
            process.castTick(this, currentTarget);

        // Check if next process should be started
        if (process == null || ct++ >= process.getCastDuration(currentTarget))
        {
            ct = 0;
            processIndex++;

            // End last process
            if (process != null)
            {
                currentTarget = process.castEnd(this, currentTarget);
            }

            // Send update to clients
            C5CastUpdate message = new C5CastUpdate(id, processIndex, currentTarget);
            MahoukaMod.getNetChannel().sendToDimension(message, caster.worldObj.provider.dimensionId);

            // Start next process
            if (processIndex < sequence.getProcesses().size())
            {
                process = sequence.getProcesses().get(processIndex);
                if (!currentTarget.matchesTypes(process.getValidTargets()))
                {
                    // TODO: Cancel cast with errors
                    cancel();
                    return;
                }
                currentTarget = process.castStart(this, currentTarget);
            }
            else
            {
                castEnd();
            }
        }
    }

    private void castTickClient()
    {
        MagicProcess process = sequence.getProcesses().get(processIndex);
        process.castTickClient(this, currentTarget);
        // MahoukaMod.proxy.castTickClient(process, this, currentTarget);
    }

    /* ------------------------------------------------------------ */

    private void castEnd()
    {
        active = false;
        // TODO Auto-generated method stub
    }

    private void castEndClient()
    {
        active = false;
        // TODO Auto-generated method stub
    }

    /* ------------------------------------------------------------ */

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

    public World getWorld()
    {
        return caster.worldObj;
    }

    public Target getTarget()
    {
        return target;
    }

    public Target getCurrentTarget()
    {
        return currentTarget;
    }

    public boolean isActive()
    {
        return active;
    }

    public boolean isServer()
    {
        return !caster.worldObj.isRemote;
    }

    public boolean isClient()
    {
        return caster.worldObj.isRemote;
    }

    public int getCurrentProcess()
    {
        return processIndex;
    }

}
