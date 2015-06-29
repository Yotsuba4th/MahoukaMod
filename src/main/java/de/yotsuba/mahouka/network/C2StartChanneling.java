package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.BufUtils;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

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
        UUID cadId = BufUtils.uuidFromBytes(buf);
        EntityPlayer caster = Utils.getClientPlayerByUuid(BufUtils.uuidFromBytes(buf));
        Target target = Target.fromBytes(Minecraft.getMinecraft().theWorld, buf);
        ActivationSequence sequence = new ActivationSequence(ByteBufUtils.readTag(buf));

        cast = new CastingProcess(caster, sequence, target, cadId);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        BufUtils.uuidToBytes(buf, cast.getId());
        BufUtils.uuidToBytes(buf, cast.getCaster().getPersistentID());
        cast.getTarget().toBytes(buf);
        ByteBufUtils.writeTag(buf, cast.getSequence().writeToNBT());
    }

    @Override
    public IMessage onMessage(C2StartChanneling message, MessageContext ctx)
    {
        MahoukaMod.getCastingManagerClient().startChanneling(message.cast);
        return null;
    }

    public static void send(CastingProcess cast)
    {
        C2StartChanneling message = new C2StartChanneling(cast);
        Vec3 pos = cast.getTarget().toTargetPoint().getPoint();
        TargetPoint tpoint = new TargetPoint(cast.getCaster().worldObj.provider.dimensionId, pos.xCoord, pos.yCoord, pos.zCoord, 16 * 64);
        MahoukaMod.getNetChannel().sendToAllAround(message, tpoint);
    }

}
