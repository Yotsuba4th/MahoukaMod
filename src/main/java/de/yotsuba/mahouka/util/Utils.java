package de.yotsuba.mahouka.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;

public final class Utils
{

    public static final int TICKS_PER_SECOND = 20;

    public static int millisecondsToTicks(int milliseconds)
    {
        return secondsToTicks(milliseconds) / 1000;
    }

    public static int secondsToTicks(int seconds)
    {
        return TICKS_PER_SECOND * seconds;
    }

    public static NBTTagCompound getPlayerPersistedTag(EntityPlayer player)
    {
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
        return tag;
    }

    @SuppressWarnings("unchecked")
    public static List<EntityPlayerMP> getPlayerList()
    {
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    }

    public static EntityPlayer getPlayerByUuid(UUID uuid)
    {
        for (EntityPlayer player : getPlayerList())
            if (player.getPersistentID().equals(uuid))
                return player;
        return null;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public static List<EntityPlayer> getClientPlayerList()
    {
        return Minecraft.getMinecraft().theWorld.playerEntities;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayerByUuid(UUID uuid)
    {
        for (EntityPlayer player : getClientPlayerList())
            if (player.getPersistentID().equals(uuid))
                return player;
        return null;
    }

    public static List<Slot> getPlayerContainerSlots(InventoryPlayer playerInventory)
    {
        List<Slot> slots = new ArrayList<Slot>();
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                slots.add(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            slots.add(new Slot(playerInventory, i, 8 + i * 18, 142));
        return slots;
    }

    public static int registerEntity(Class<? extends Entity> entityClass, String name)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, MahoukaMod.instance, 64, 1, true);
        return entityID;
    }

    @SuppressWarnings("unchecked")
    public static int registerMob(Class<? extends Entity> entityClass, String name)
    {
        int entityID = registerEntity(entityClass, name);
        long seed = name.hashCode();
        Random rand = new Random(seed);
        int primaryColor = rand.nextInt() * 16777215;
        int secondaryColor = rand.nextInt() * 16777215;
        EntityList.entityEggs.put(entityID, new EntityList.EntityEggInfo(entityID, primaryColor, secondaryColor));
        return entityID;
    }

    public static boolean mergeItemStack(List<Slot> inventorySlots, ItemStack stack, int fromSlot, int toSlot, boolean backward)
    {
        boolean mergedSomething = false;
        int index = backward ? toSlot - 1 : fromSlot;
        Slot slot;
        ItemStack slotStack;
        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && (!backward && index < toSlot || backward && index >= fromSlot))
            {
                slot = inventorySlots.get(index);
                slotStack = slot.getStack();
                if (slot.isItemValid(stack))
                {
                    int slotLimit = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
                    if (slotStack != null && slotStack.getItem() == stack.getItem()
                            && (!stack.getHasSubtypes() || stack.getItemDamage() == slotStack.getItemDamage())
                            && ItemStack.areItemStackTagsEqual(stack, slotStack))
                    {
                        int total = slotStack.stackSize + stack.stackSize;
                        if (total <= slotLimit)
                        {
                            stack.stackSize = 0;
                            slotStack.stackSize = total;
                            slot.onSlotChanged();
                            mergedSomething = true;
                        }
                        else if (slotStack.stackSize < slotLimit)
                        {
                            stack.stackSize -= slotLimit - slotStack.stackSize;
                            slotStack.stackSize = slotLimit;
                            slot.onSlotChanged();
                            mergedSomething = true;
                        }
                    }
                }
    
                if (backward)
                    --index;
                else
                    ++index;
            }
        }
    
        if (stack.stackSize > 0)
        {
            index = backward ? toSlot - 1 : fromSlot;
            while (!backward && index < toSlot || backward && index >= fromSlot)
            {
                slot = inventorySlots.get(index);
                if (slot.isItemValid(stack))
                {
                    slotStack = slot.getStack();
                    if (slotStack == null)
                    {
                        int amount = Math.min(stack.stackSize, slot.getSlotStackLimit());
                        slot.putStack(stack.splitStack(amount));
                        slot.onSlotChanged();
                        mergedSomething = true;
                        if (stack.stackSize <= 0)
                            break;
                    }
                }
    
                if (backward)
                    --index;
                else
                    ++index;
            }
        }
        return mergedSomething;
    }

}
