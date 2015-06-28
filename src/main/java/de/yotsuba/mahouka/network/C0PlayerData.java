package de.yotsuba.mahouka.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.util.Utils;

public class C0PlayerData implements IMessage, IMessageHandler<C0PlayerData, IMessage>
{

    private NBTTagCompound tag;

    public C0PlayerData()
    {
    }

    public C0PlayerData(NBTTagCompound tag)
    {
        this.tag = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(C0PlayerData message, MessageContext ctx)
    {
        Utils.getPlayerPersistedTag(Minecraft.getMinecraft().thePlayer).setTag(PlayerData.TAG_NAME, message.tag);
        return null;
    }

    public static void send(EntityPlayerMP player)
    {
        C0PlayerData message = new C0PlayerData(Utils.getPlayerPersistedTag(player).getCompoundTag(PlayerData.TAG_NAME));
        MahoukaMod.getNetChannel().sendTo(message, player);
    }

}
