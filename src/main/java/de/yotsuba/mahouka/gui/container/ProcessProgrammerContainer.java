package de.yotsuba.mahouka.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.yotsuba.mahouka.gui.ButtenClickListener;
import de.yotsuba.mahouka.gui.slot.SlotProcess;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.util.Utils;

public class ProcessProgrammerContainer extends Container implements ButtenClickListener
{

    private InventoryBasic invProcess;

    public boolean needGuiUpdate;

    private static final int PROCESS_SLOT = 0;

    private MagicProcess process;

    public ProcessProgrammerContainer(InventoryPlayer playerInventory)
    {
        invProcess = new InventoryBasic("Process", false, 1);
        addSlotToContainer(new SlotProcess(this, invProcess, PROCESS_SLOT, 8, 8));
        for (Slot slot : Utils.getPlayerContainerSlots(playerInventory))
            addSlotToContainer(slot);
    }

    public MagicProcess getProcess()
    {
        return process;
    }

    public void processChanged()
    {
        needGuiUpdate = true;
        ItemStack stack = getSlot(0).getStack();
        if (stack != null)
            process = ((ItemMagicProcess) stack.getItem()).getProcess(stack);
        else
            process = null;
    }

    public void updateItemStack()
    {
        ItemStack stack = getSlot(0).getStack();
        if (stack != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            process.writeToNBT(tag);
            stack.setTagCompound(tag);
        }
    }

    @Override
    public void buttonClicked(int id)
    {
        if (process != null)
        {
            process.guiButtonClick(id);
            updateItemStack();
        }
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
