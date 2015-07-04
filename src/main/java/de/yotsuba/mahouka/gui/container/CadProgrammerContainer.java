package de.yotsuba.mahouka.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.gui.slot.SlotCad;
import de.yotsuba.mahouka.gui.slot.SlotSequence;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.util.Utils;

public class CadProgrammerContainer extends Container
{

    public static final int SEQ_SLOT_WIDTH = 9;
    public static final int SEQ_SLOT_HEIGHT = 3;
    public static final int SEQ_SLOT_COUNT = SEQ_SLOT_WIDTH * SEQ_SLOT_HEIGHT;

    protected InventoryBasic invCad = new InventoryBasic("CAD", false, 1);

    protected CadBase cad;

    public CadProgrammerContainer(InventoryPlayer playerInventory)
    {
        addSlotToContainer(new SlotCad(this, invCad, 0, 8, 8));

        for (int iy = 0; iy < SEQ_SLOT_HEIGHT; ++iy)
            for (int ix = 0; ix < SEQ_SLOT_WIDTH; ++ix)
                addSlotToContainer(new SlotSequence(this, ix + iy * SEQ_SLOT_WIDTH, 8 + ix * 18, 26 + iy * 18));

        for (Slot slot : Utils.getPlayerContainerSlots(playerInventory))
            addSlotToContainer(slot);
    }

    public void onCadChanged()
    {
        ItemStack cadStack = invCad.getStackInSlot(0);
        cad = (cadStack == null) ? null : CadManager.getCad(cadStack);
    }

    public CadBase getCad()
    {
        return cad;
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        ItemStack stackCad = invCad.getStackInSlot(0);
        if (stackCad != null)
            player.dropPlayerItemWithRandomChoice(stackCad, false);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        Slot slot = (Slot) inventorySlots.get(index);
        if (slot == null)
            return null;

        ItemStack slotStack = slot.getStack();
        if (slotStack == null)
            return null;

        ItemStack resultStack = slotStack.copy();

        CadBase cad = getCad();
        if (cad == null)
        {
            if (!(slotStack.getItem() instanceof ItemCad))
                return null;
            if (index < 1 + SEQ_SLOT_COUNT)
                return null;
            if (invCad.getStackInSlot(0) != null)
                return null;
            if (!mergeItemStack(slotStack, 0, 1, false))
                return null;
        }
        else
        {
            if (index == 0)
            {
                int start = 1 + SEQ_SLOT_COUNT + player.inventory.mainInventory.length - 9;
                int end = start + 9;
                if (!mergeItemStack(slotStack, start, end, false))
                {
                    start = 1 + SEQ_SLOT_COUNT;
                    end = start + player.inventory.mainInventory.length;
                    if (!mergeItemStack(slotStack, start, end, true))
                        return null;
                }
            }
            else if (index < 1 + SEQ_SLOT_COUNT)
            {
                int start = 1 + SEQ_SLOT_COUNT;
                int end = start + player.inventory.mainInventory.length;
                if (!mergeItemStack(slotStack, start, end, true))
                    return null;
            }
            else
            {
                if (!mergeItemStack(slotStack, 1, 1 + SEQ_SLOT_COUNT, false))
                    return null;
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
    @SuppressWarnings("unchecked")
    protected boolean mergeItemStack(ItemStack stack, int fromSlot, int toSlot, boolean backward)
    {
        return Utils.mergeItemStack(inventorySlots, stack, fromSlot, toSlot, backward);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

}
