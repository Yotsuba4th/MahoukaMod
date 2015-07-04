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

    protected InventoryBasic invCad = new InventoryBasic("CAD", false, 1);

    protected CadBase cad;

    public CadProgrammerContainer(InventoryPlayer playerInventory)
    {
        addSlotToContainer(new SlotCad(this, invCad, 0, 8, 8));

        for (int iy = 0; iy < 3; ++iy)
            for (int ix = 0; ix < 9; ++ix)
                addSlotToContainer(new SlotSequence(this, ix + iy * 9, 8 + ix * 18, 26 + iy * 18));

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

        if (cad != null)
            cad.readFromItems();

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

        CadBase cad = getCad();
        if (cad == null)
        {
            return slotStack;
        }

        ItemStack resultStack = slotStack.copy();
        if (index < 1 + cad.getSizeInventory())
        {
            int start = 1 + cad.getSizeInventory();
            int end = start + player.inventory.mainInventory.length;
            if (!mergeItemStack(slotStack, start, end, true))
                return null;
        }
        else
        {
            if (slotStack.getItem() instanceof ItemCad)
            {
                if (!mergeItemStack(slotStack, 0, 1, false))
                    return null;
            }
            else if (true)
            {
                if (!mergeItemStack(slotStack, 1, 1 + cad.getSizeInventory(), false))
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
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

}
