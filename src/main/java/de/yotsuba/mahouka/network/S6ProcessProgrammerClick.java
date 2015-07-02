package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.gui.container.ProcessProgrammerContainer;

public class S6ProcessProgrammerClick implements IMessage, IMessageHandler<S6ProcessProgrammerClick, IMessage>
{

    private int id;
    private int x;
    private int y;
    private int z;

    public S6ProcessProgrammerClick()
    {
    }

    public S6ProcessProgrammerClick(int id, int x, int y, int z)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(id);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public IMessage onMessage(S6ProcessProgrammerClick message, MessageContext ctx)
    {
        ModContainer mc = FMLCommonHandler.instance().findContainerFor(MahoukaMod.instance);
        EntityPlayerMP player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
        Container remoteGuiContainer = NetworkRegistry.INSTANCE.getRemoteGuiContainer(mc, player, message.id, player.worldObj, x, y, z);
        if (remoteGuiContainer instanceof ProcessProgrammerContainer)
        {
            ProcessProgrammerContainer container = (ProcessProgrammerContainer) remoteGuiContainer;
            container.buttonClicked(message.id);
        }
        return null;
    }

    public static void send(int id, int x, int y, int z)
    {
        MahoukaMod.getNetChannel().sendToServer(new S6ProcessProgrammerClick(id, x, y, z));
    }

}
