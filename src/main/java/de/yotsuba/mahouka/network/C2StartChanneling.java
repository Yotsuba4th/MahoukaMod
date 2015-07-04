package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cast.CastingProcess;

public class C2StartChanneling implements IMessage, IMessageHandler<C2StartChanneling, IMessage>
{

    private CastingProcess cast;

    public C2StartChanneling()
    {
    }

    public C2StartChanneling(CastingProcess cast)
    {
        this.cast = cast;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void fromBytes(ByteBuf buf)
    {
        cast = CastingProcess.fromBytes(Minecraft.getMinecraft().theWorld, buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        cast.toBytes(buf);
    }

    @Override
    public IMessage onMessage(C2StartChanneling message, MessageContext ctx)
    {
        if (message.cast != null)
            message.cast.startClient();
        return null;
    }

    public static void send(CastingProcess cast)
    {
        C2StartChanneling message = new C2StartChanneling(cast);
        Vec3 pos = cast.getTarget().getCurrentPoint();
        TargetPoint tpoint = new TargetPoint(cast.getCaster().worldObj.provider.dimensionId, pos.xCoord, pos.yCoord, pos.zCoord, 16 * 64);
        MahoukaMod.getNetChannel().sendToAllAround(message, tpoint);
    }

}
