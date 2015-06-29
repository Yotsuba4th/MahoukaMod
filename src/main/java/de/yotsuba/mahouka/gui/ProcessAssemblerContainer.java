package de.yotsuba.mahouka.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ProcessAssemblerContainer extends Container
{
    public ProcessAssemblerContainer(InventoryPlayer playerInventory)
    {
        // TODO: Add texture corresponding item slots

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

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

}
