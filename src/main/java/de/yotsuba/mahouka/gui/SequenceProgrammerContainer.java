package de.yotsuba.mahouka.gui;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.yotsuba.mahouka.item.ItemMagicProcess;

public class SequenceProgrammerContainer extends Container implements IInventory
{
    private final EntityPlayer player;

    public SequenceProgrammerContainer(InventoryPlayer playerInventory)
    {
        player = playerInventory.player;

        addSlotToContainer(new Slot(this, 0, 49, 18));
        addSlotToContainer(new Slot(this, 1, 49, 54));
        addSlotToContainer(new Slot(this, 2, 107, 36));

        addInventoryToContainer(playerInventory);
    }

    private void addInventoryToContainer(InventoryPlayer playerInventory)
    {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (int i = 0; i < 9; ++i)
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        Slot slotObject = (Slot) inventorySlots.get(slot);

        // null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject == null || !slotObject.getHasStack())
            return null;

        // merges the item into player inventory since its in the tileEntity
        ItemStack stackInSlot = slotObject.getStack();
        if (slot < inventoryItemStacks.size())
        {
            if (!this.mergeItemStack(stackInSlot, inventoryItemStacks.size(), player.inventory.mainInventory.length + inventoryItemStacks.size(), true))
                return null;
        }
        // places it into the tileEntity is possible since its in the player inventory
        else
        {
            if (!this.mergeItemStack(stackInSlot, 0, inventoryItemStacks.size(), false))
                return null;
        }

        if (stackInSlot.stackSize == 0)
            slotObject.putStack(null);
        else
            slotObject.onSlotChanged();

        ItemStack stack = stackInSlot.copy();
        if (stackInSlot.stackSize == stack.stackSize)
            return null;
        slotObject.onPickupFromSlot(player, stackInSlot);

        return stack;
    }

    /* ------------------------------------------------------------ */

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        Object o = inventoryItemStacks.get(slot);
        if (o instanceof ItemStack)
            return (ItemStack) o;
        else
            return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemStack decrStackSize(int slot, int amount)
    {
        if (inventoryItemStacks.get(slot) == null)
            return null;

        ItemStack slotItem = null;
        if (inventoryItemStacks.get(slot) instanceof ItemStack)
            slotItem = (ItemStack) inventoryItemStacks.get(slot);

        ItemStack stack;
        if (slotItem.stackSize <= amount)
        {
            stack = slotItem;
            inventoryItemStacks.set(slot, null);
        }
        else
        {
            stack = slotItem.splitStack(amount);
            if (slotItem.stackSize == 0)
                inventoryItemStacks.set(slot, null);
        }
        return stack;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (inventoryItemStacks.get(slot) == null)
            return null;

        ItemStack stack = null;
        if (inventoryItemStacks.get(slot) instanceof ItemStack)
            stack = (ItemStack) inventoryItemStacks.get(slot);
        inventoryItemStacks.set(slot, null);
        return stack;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventoryItemStacks.set(slot, stack);

        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInventoryName()
    {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
        if (!player.worldObj.isRemote)
        {
            Random rand = new Random();
            for (int slotIndex = 0; slotIndex < getSizeInventory(); ++slotIndex)
            {
                ItemStack itemstack = getStackInSlot(slotIndex);
                if (itemstack != null)
                {
                    float randX = rand.nextFloat() * 0.8F + 0.1F;
                    float randY = rand.nextFloat() * 0.8F + 0.1F;
                    float randZ = rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int amount = rand.nextInt(21) + 10;
                        if (amount > itemstack.stackSize)
                            amount = itemstack.stackSize;
                        itemstack.stackSize -= amount;
                        EntityItem entityitem = new EntityItem(player.worldObj, player.posX + randX, player.posY + randY, player.posZ + randZ, new ItemStack(
                                itemstack.getItem(), amount, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());

                        float velocity = 0.05F;
                        entityitem.motionX = (float) rand.nextGaussian() * velocity;
                        entityitem.motionY = (float) rand.nextGaussian() * velocity + 0.2F;
                        entityitem.motionZ = (float) rand.nextGaussian() * velocity;
                        player.worldObj.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        // TODO: Better isItemValidForSlot check
        return stack.getItem() instanceof ItemMagicProcess;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        closeInventory();
    }

}
