package de.yotsuba.mahouka.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.gui.slot.SlotProcess;
import de.yotsuba.mahouka.util.Utils;

public class ProcessProgrammerContainer extends Container
{

    private InventoryBasic invProcess;

    public boolean needGuiUpdate;

    private static final int PROCESS_SLOT = 0;

    public ProcessProgrammerContainer(InventoryPlayer playerInventory)
    {
        invProcess = new InventoryBasic("Process", false, 1);

        addSlotToContainer(new SlotProcess(this, invProcess, PROCESS_SLOT, 8, 8));

        for (Slot slot : Utils.getPlayerContainerSlots(playerInventory))
            addSlotToContainer(slot);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
        Slot slot = (Slot) inventorySlots.get(slotID);
        if (slot == null || !slot.getHasStack())
            return null;

        ItemStack slotStack = slot.getStack();
        ItemStack resultStack = slotStack.copy();
        if (slotID < invProcess.getSizeInventory())
        {
            int start = invProcess.getSizeInventory();
            int end = start + player.inventory.mainInventory.length;
            if (!mergeItemStack(slotStack, start, end, true))
                return null;
            else
            {
                // TODO: Unload process specific gui elements
            }
        }
        else
        {
            if (((Slot) inventorySlots.get(0)).isItemValid(slotStack))
            {
                if (getSlot(0).getHasStack())
                    return null;
                ItemStack itemToMerge = slotStack.splitStack(1);
                if (!mergeItemStack(itemToMerge, 0, 1, false))
                {
                    slotStack.stackSize++;
                    return null;
                }
                else
                {
                    // TODO: Load process specific gui elements
                }
            }
        }

        if (slotStack.stackSize == 0)
            slot.putStack((ItemStack) null);
        else
            slot.onSlotChanged();

        if (slotStack.stackSize == resultStack.stackSize)
            return null;
        slot.onPickupFromSlot(player, slotStack);

        return resultStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        ItemStack stackProcess = invProcess.getStackInSlot(0);
        if (stackProcess != null)
            player.dropPlayerItemWithRandomChoice(stackProcess, false);
    }
}
