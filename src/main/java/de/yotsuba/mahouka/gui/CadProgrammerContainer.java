package de.yotsuba.mahouka.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.util.Utils;

public class CadProgrammerContainer extends Container
{

    private InventoryBasic invCad;

    private InventoryBasic invSequences;

    private ItemStack[] sequences;

    public CadProgrammerContainer(InventoryPlayer playerInventory)
    {
        invCad = new InventoryBasic("CAD", false, 1);
        invSequences = new InventoryBasic("MS", false, 9 * 3);

        addSlotToContainer(new SlotCad(this, invCad, 0, 8, 8));
        for (int iy = 0; iy < 3; ++iy)
            for (int ix = 0; ix < 9; ++ix)
                addSlotToContainer(new Slot(invSequences, ix + iy * 9, 8 + ix * 18, 26 + iy * 18));
        for (Slot slot : Utils.getPlayerContainerSlots(playerInventory))
            addSlotToContainer(slot);
    }

    protected void cadToSequences()
    {
        sequences = null;
        ItemStack cadStack = invCad.getStackInSlot(0);
        if (cadStack == null)
            return;
        CadBase cad = CadManager.getCad(cadStack);

        sequences = new ItemStack[cad.getActivationSequences().length];
        for (int i = 0; i < sequences.length; i++)
        {
            ItemStack seqStack = null;
            ActivationSequence seq = cad.getActivationSequences()[i];
            if (seq != null)
            {
                seqStack = seq.getItemStack();
                invSequences.setInventorySlotContents(i, seqStack);
            }
            sequences[i] = seqStack;
            cad.getActivationSequences()[i] = null;
        }

        // TODO: Handle scrolling
        for (int i = 0; i < invSequences.getSizeInventory(); i++)
            invSequences.setInventorySlotContents(i, i < sequences.length ? sequences[i] : null);
    }

    protected void sequencesToCad(ItemStack cadStack)
    {
        if (cadStack == null)
            return;
        CadBase cad = CadManager.getCad(cadStack);

        for (int i = 0; i < sequences.length; i++)
        {
            ActivationSequence seq = null;
            ItemStack seqStack = invSequences.getStackInSlot(i);
            if (seqStack != null)
            {
                seq = new ActivationSequence();
                seq.readFromNBT(seqStack.getTagCompound());
            }
            cad.getActivationSequences()[i] = seq;
        }
        sequences = null;
        cad.writeToNBT(cadStack.getTagCompound());

        for (int i = 0; i < invSequences.getSizeInventory(); i++)
            invSequences.setInventorySlotContents(i, null);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        ItemStack stackCad = invCad.getStackInSlot(0);
        sequencesToCad(stackCad);
        if (stackCad != null)
            player.dropPlayerItemWithRandomChoice(stackCad, false);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        return null;
        // Slot slotObject = (Slot) inventorySlots.get(slot);
        //
        // // null checks and checks if the item can be stacked (maxStackSize > 1)
        // if (slotObject == null || !slotObject.getHasStack())
        // return null;
        //
        // // merges the item into player inventory since its in the tileEntity
        // ItemStack stackInSlot = slotObject.getStack();
        // if (slot < inventoryItemStacks.size())
        // {
        // if (!this.mergeItemStack(stackInSlot, inventoryItemStacks.size(), player.inventory.mainInventory.length + inventoryItemStacks.size(), true))
        // return null;
        // }
        // // places it into the tileEntity is possible since its in the player inventory
        // else
        // {
        // if (!this.mergeItemStack(stackInSlot, 0, inventoryItemStacks.size(), false))
        // return null;
        // }
        //
        // if (stackInSlot.stackSize == 0)
        // slotObject.putStack(null);
        // else
        // slotObject.onSlotChanged();
        //
        // ItemStack stack = stackInSlot.copy();
        // if (stackInSlot.stackSize == stack.stackSize)
        // return null;
        // slotObject.onPickupFromSlot(player, stackInSlot);
        //
        // return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

}
