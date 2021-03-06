package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;

public class S1StartChanneling implements IMessage, IMessageHandler<S1StartChanneling, IMessage>
{

    private Target target;

    // private UUID id;

    private ByteBuf buf;

    public S1StartChanneling()
    {
    }

    public S1StartChanneling(Target target)
    {
        // this.id = id;
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.buf = buf;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        // BufUtils.uuidToBytes(buf, id);
        target.toBytes(buf);
    }

    @Override
    public IMessage onMessage(S1StartChanneling message, MessageContext ctx)
    {
        EntityPlayerMP caster = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
        // UUID id = BufUtils.uuidFromBytes(message.buf);
        Target target = Target.fromBytes(caster.worldObj, message.buf);
        message.buf = null;
        if (target == null)
            return null;

        CadBase cad = CadManager.getCad(caster.getCurrentEquippedItem());
        if (cad == null || cad.getSelectedSequence() == null)
        {
            // TODO (5) Error handling
            return null;
        }

        CastingProcess cast = CastingProcess.create(caster, cad.getSelectedSequence(), target, cad.getId());
        if (cast == null)
        {
            // TODO (5) Error handling
            return null;
        }
        cast.start();
        return null;
    }

    public static void send(Target target)
    {
        S1StartChanneling message = new S1StartChanneling(target);
        MahoukaMod.getNetChannel().sendToServer(message);
    }

}
