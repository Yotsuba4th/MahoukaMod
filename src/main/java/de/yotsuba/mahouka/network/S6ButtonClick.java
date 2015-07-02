package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.gui.ButtenClickListener;

public class S6ButtonClick implements IMessage, IMessageHandler<S6ButtonClick, IMessage>
{

    private int id;

    /* ------------------------------------------------------------ */

    public S6ButtonClick()
    {
    }

    public S6ButtonClick(int id)
    {
        this.id = id;
    }

    /* ------------------------------------------------------------ */

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

    /* ------------------------------------------------------------ */

    @Override
    public IMessage onMessage(S6ButtonClick message, MessageContext ctx)
    {
        EntityPlayerMP player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
        if (player.openContainer instanceof ButtenClickListener)
            ((ButtenClickListener) player.openContainer).buttonClicked(message.id);
        return null;
    }

    public static void send(int id)
    {
        MahoukaMod.getNetChannel().sendToServer(new S6ButtonClick(id));
    }

}
