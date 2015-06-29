package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.BufUtils;

public class C3CancelCast implements IMessage, IMessageHandler<C3CancelCast, IMessage>
{

    private UUID id;

    public C3CancelCast()
    {
    }

    public C3CancelCast(UUID id)
    {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = BufUtils.uuidFromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        BufUtils.uuidToBytes(buf, id);
    }

    @Override
    public IMessage onMessage(C3CancelCast message, MessageContext ctx)
    {
        MahoukaMod.getCastingManagerClient().cancelCast(message.id);
        return null;
    }

    public static void send(CastingProcess cast)
    {
        C3CancelCast message = new C3CancelCast(cast.getId());
        MahoukaMod.getNetChannel().sendToAll(message);
    }

}
