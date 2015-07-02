package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;

public class S6ProcessProgrammerClick implements IMessage, IMessageHandler<S6ProcessProgrammerClick, IMessage>
{

    private int id;

    public S6ProcessProgrammerClick()
    {
    }

    public S6ProcessProgrammerClick(int id)
    {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(id);
    }

    @Override
    public IMessage onMessage(S6ProcessProgrammerClick message, MessageContext ctx)
    {
        // EntityPlayerMP player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
        // NetworkRegistry.INSTANCE.getRemoteGuiContainer(mc, entityPlayerMP, modGuiId, world, x, y, z);
        // TODO: send button click to container-slot process
        return null;
    }

    public static void send(int id)
    {
        MahoukaMod.getNetChannel().sendToServer(new S6ProcessProgrammerClick(id));
    }

}
