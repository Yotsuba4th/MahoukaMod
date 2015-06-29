package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.util.BufUtils;

public class S4CancelCast implements IMessage, IMessageHandler<S4CancelCast, IMessage>
{

    private UUID id;

    public S4CancelCast()
    {
    }

    public S4CancelCast(UUID id)
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
    public IMessage onMessage(S4CancelCast message, MessageContext ctx)
    {
        MahoukaMod.getCastingManagerServer().cancelCast(id);
        return null;
    }

    public static void send(UUID id)
    {
        S4CancelCast message = new S4CancelCast(id);
        MahoukaMod.getNetChannel().sendToServer(message);
    }

}
