package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public class S1StartChanneling implements IMessage, IMessageHandler<S1StartChanneling, IMessage>
{

    private EntityPlayer caster;

    private Target target;

    private UUID cadId;

    private ByteBuf tmpBuf;

    public S1StartChanneling()
    {
    }

    public S1StartChanneling(EntityPlayer caster, UUID cadId, Target target)
    {
        this.caster = caster;
        this.cadId = cadId;
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        cadId = BufUtils.uuidFromBytes(buf);
        caster = Utils.getPlayerByUuid(BufUtils.uuidFromBytes(buf));
        tmpBuf = buf;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        BufUtils.uuidToBytes(buf, cadId);
        BufUtils.uuidToBytes(buf, caster.getPersistentID());
        target.toBytes(buf);
    }

    @Override
    public IMessage onMessage(S1StartChanneling message, MessageContext ctx)
    {
        message.target = Target.fromBytes(((NetHandlerPlayServer) ctx.netHandler).playerEntity.worldObj, message.tmpBuf);
        message.tmpBuf = null;
        if (message.target == null)
            return null;

        CadBase cad = CadManager.getCad(message.cadId);
        if (cad == null)
            return null;

        CastingProcess cast = new CastingProcess(message.caster, cad.getSelectedSequence(), message.target, message.cadId);
        MahoukaMod.getCastingManagerServer().startChanneling(cast);
        return null;
    }

    public static void send(EntityPlayer caster, UUID cadId, Target target)
    {
        S1StartChanneling message = new S1StartChanneling(caster, cadId, target);
        MahoukaMod.getNetChannel().sendToServer(message);
    }

}
