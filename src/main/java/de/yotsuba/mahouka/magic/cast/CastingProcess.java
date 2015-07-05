package de.yotsuba.mahouka.magic.cast;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.effect.EffectChanneling;
import de.yotsuba.mahouka.client.effect.EffectRenderer;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.process.ProcessSequence;
import de.yotsuba.mahouka.network.C2StartChanneling;
import de.yotsuba.mahouka.network.C3CancelCast;
import de.yotsuba.mahouka.network.C5CastUpdate;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;

public class CastingProcess
{

    private ProcessSequence sequence;

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

    public CastingProcess(EntityPlayer caster, MagicProcess sequence, Target target, UUID id, int psion, int channelTime)
    {
        this.id = id;
        this.caster = caster;
        this.target = target;
        this.psion = psion;
        this.channelTime = channelTime;
        if (sequence.getClass().equals(ProcessSequence.class))
        {
            this.sequence = (ProcessSequence) sequence;
        }
        else
        {
            this.sequence = new ProcessSequence();
            this.sequence.getProcesses().add(sequence);
        }
        t = 0;
        currentTarget = target;
    }

    public static CastingProcess create(EntityPlayer caster, MagicProcess sequence, Target target, UUID id)
    {
        int channelingDuration = sequence.getChannelingDuration();
        int psionCost = sequence.getPsionCost();
        CastingProcess cast = new CastingProcess(caster, sequence, target, id, psionCost, channelingDuration);
        return cast;
    }

    /* ------------------------------------------------------------ */

    public static CastingProcess fromBytes(World world, ByteBuf buf)
    {
        UUID id = BufUtils.uuidFromBytes(buf);
        EntityPlayer caster = Utils.getClientPlayerByUuid(BufUtils.uuidFromBytes(buf));
        Target target = Target.fromBytes(world, buf);
        MagicProcess sequence = MagicProcess.createFromStack(ByteBufUtils.readItemStack(buf));
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
        ByteBufUtils.writeItemStack(buf, sequence.getItemStack());
        buf.writeInt(psion);
        buf.writeInt(channelTime);
    }

    /* ------------------------------------------------------------ */

    public boolean start()
    {
        if (CastingManager.isServerCasting(id))
        {
            // TODO (6) Translation
            caster.addChatMessage(new ChatComponentText("Another magic is still active!"));
            return false;
        }
        C2StartChanneling.send(this);
        CastingManager.serverCasts.put(id, this);
        return true;
    }

    public void startClient()
    {
        CastingManager.clientCasts.put(id, this);
    }

    /* ------------------------------------------------------------ */

    public boolean cancel(boolean force)
    {
        if (processIndex >= 0 && processIndex < sequence.getProcesses().size())
        {
            MagicProcess process = sequence.getProcesses().get(processIndex);
            if (!process.castCancel(this, currentTarget) && !force)
            {
                return false;
            }
        }
        active = false;
        CastingManager.serverCasts.remove(id);
        C3CancelCast.send(this);
        if (MahoukaMod.DEBUG)
            MahoukaMod.getLogger().info("Cancelled cast {}", id);
        return true;
    }

    public void cancelClient()
    {
        EffectRenderer.cancelEffects(id);
        active = false;
        CastingManager.clientCasts.remove(id);
        if (MahoukaMod.DEBUG)
            MahoukaMod.getLogger().info("Cancelled client cast {}", id);
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

    public void updateClient(int newProcess, Target target)
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

    private void channelStart()
    {
        /* do nothing */
    }

    private void channelStartClient()
    {
        if (channelTime > 0)
        {
            EffectChanneling fx = new EffectChanneling(new TargetEntity(caster, true, false));
            fx.fadeOut = 20;
            fx.maxAge = channelTime + fx.fadeOut;
            EffectRenderer.addEffect(fx, id);

            // TODO (2) Start channeling effect on CAD
            
            fx = new EffectChanneling(target);
            fx.maxAge = channelTime + fx.fadeOut;
            EffectRenderer.addEffect(fx, id);
        }
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
        // TODO (3) Channel end sound
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
                    // TODO (5) Cast cancel error / sound
                    caster.addChatMessage(new ChatComponentText("Cast failed!"));
                    cancel(true);
                    return;
                }
                currentTarget = process.castStart(this, currentTarget);
                if (currentTarget == null)
                    cancel(true);
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
    }

    private void castEndClient()
    {
        active = false;
    }

    /* ------------------------------------------------------------ */

    public UUID getId()
    {
        return id;
    }

    public ProcessSequence getSequence()
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
