package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.target.Target;

public class C5CastUpdate implements IMessage, IMessageHandler<C5CastUpdate, IMessage>
{

    private UUID id;

    private int process;

    private Target target;

    private ByteBuf buf;

    public C5CastUpdate()
    {
    }

    public C5CastUpdate(UUID id, int process, Target target)
    {
        this.id = id;
        this.process = process;
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
        BufUtils.uuidToBytes(buf, id);
        buf.writeInt(process);
        target.toBytes(buf);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(C5CastUpdate message, MessageContext ctx)
    {
        UUID id = BufUtils.uuidFromBytes(message.buf);
        int process = message.buf.readInt();
        Target target = Target.fromBytes(Minecraft.getMinecraft().theWorld, message.buf);
        MahoukaMod.getCastingManagerClient().castUpdate(id, process, target);
        return null;
    }

}
