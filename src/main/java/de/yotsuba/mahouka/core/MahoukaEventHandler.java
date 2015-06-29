package de.yotsuba.mahouka.core;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.CadManager;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.util.Utils;

public class MahoukaEventHandler
{

    public MahoukaEventHandler()
    {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerLoggedInEvent(PlayerLoggedInEvent event)
    {
        C0PlayerData.send((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void serverTickEvent(ServerTickEvent event)
    {
        if (event.phase == Phase.END)
            return;

        if (MinecraftServer.getServer().getEntityWorld().getWorldInfo().getWorldTotalTime() % 200 == 0)
        {
            for (EntityPlayerMP player : Utils.getPlayerList())
            {
                PlayerData data = new PlayerData(player);
                data.setPsion(data.getPsion() + 100);
                data.sendUpdate();

                for (ItemStack stack : player.inventory.mainInventory)
                {
                    if (stack == null)
                        continue;
                    if (stack.getItem() instanceof ItemCad)
                        CadManager.getCad(stack).updateItemStack(stack, player);
                }
            }
        }
    }

    @SubscribeEvent
    public void entityInteractEvent(EntityInteractEvent event)
    {
        if (event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemCad)
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void playerInteractEvent(PlayerInteractEvent event)
    {
        if (event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemCad)
        {
            event.useBlock = Result.DENY;
        }
    }

}
