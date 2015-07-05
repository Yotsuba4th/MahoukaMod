package de.yotsuba.mahouka.gui.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.gui.slot.SlotSeqProgrammerResult;
import de.yotsuba.mahouka.magic.ProcessAssembler;
import de.yotsuba.mahouka.util.Utils;

public class SequenceProgrammerContainer extends Container
{

    public static final int IN1 = 0;

    public static final int IN2 = 1;

    public static final int OUT = 2;

    /* ------------------------------------------------------------ */

    public InventoryBasic invInput = new InventoryBasic("IN", false, 2);
    public InventoryBasic invOutput = new InventoryBasic("OUT", false, 1);

    private ItemStack[] lastInv = new ItemStack[invInput.getSizeInventory() + invOutput.getSizeInventory()];

    public enum State
    {
        // ERROR should never happen
        ERROR, IDLE, COMPOSING, DECOMPOSING, DECOMPOSE_PENDING;
    }

    public State state = State.IDLE;

    public boolean needCraftingUpdate = true;

    public int pendingCount;
    public int pendingSlot;

    /* ------------------------------------------------------------ */

    public SequenceProgrammerContainer(InventoryPlayer playerInventory)
    {
        // input slots
        for (int i = 0; i < 2; i++)
            addSlotToContainer(new SlotSeqProgrammerResult(this, invInput, i, 49, 18 + i * 36));

        // output slot
        addSlotToContainer(new SlotSeqProgrammerResult(this, invOutput, 0, 107, 36));

        // player inventory
        for (Slot slot : Utils.getPlayerContainerSlots(playerInventory))
            addSlotToContainer(slot);
    }

    /* ------------------------------------------------------------ */

    public void updateCrafting()
    {
        needCraftingUpdate = false;

        // XXX: atm only changed[OUT] used
        boolean[] changed = new boolean[lastInv.length];
        for (int i = 0; i < changed.length; i++)
        {
            if (!getSlot(i).getHasStack() || getSlot(i).getStack().stackSize == 0)
                putStackInSlot(i, null);
            changed[i] = !ItemStack.areItemStacksEqual(lastInv[i], getSlot(i).getStack());
        }

        switch (state)
        {
        case DECOMPOSING:
            if (getSlot(IN1).getHasStack() ^ getSlot(IN2).getHasStack())
            {
                getSlot(OUT).decrStackSize(1);
                pendingSlot = getSlot(IN1).getHasStack() ? IN1 : IN2;
                pendingCount = getSlot(pendingSlot).getStack().stackSize;
            }

            if (!getSlot(OUT).getHasStack())
            {
                state = State.IDLE;
                if (pendingCount == 0)
                {
                    putStackInSlot(IN1, null);
                    putStackInSlot(IN2, null);
                }
                break;
            }

            ItemStack in1 = ItemStack.copyItemStack(getSlot(OUT).getStack());
            ItemStack in2 = ProcessAssembler.split(in1);
            // TODO: Unwrapped sequences don't stack with original ones
            in1 = ProcessAssembler.unwrapSequence(in1);
            in1.stackSize = 1 + (pendingSlot == IN1 ? pendingCount : 0);
            in2.stackSize = 1 + (pendingSlot == IN2 ? pendingCount : 0);
            putStackInSlot(IN1, in1);
            putStackInSlot(IN2, in2);
            break;
        case COMPOSING:
            if (changed[OUT])
                for (int i = 0; i < invInput.getSizeInventory(); i++)
                    getSlot(i).decrStackSize(1);

            if (!(getSlot(IN1).getHasStack() || getSlot(IN2).getHasStack()))
            {
                state = State.IDLE;
                putStackInSlot(OUT, null);
                break;
            }
            else
            {
                ItemStack input1 = getSlot(IN1).getStack();
                ItemStack input2 = getSlot(IN2).getStack();
                putStackInSlot(OUT, ProcessAssembler.combine(input1, input2));
            }
            break;
        default:
            break;
        }

        // if IDLE (or ERROR), update state and recall updateCrafting
        if (state == State.IDLE || state == State.ERROR)
        {
            boolean inputFilled = getSlot(IN1).getHasStack() || getSlot(IN2).getHasStack();
            boolean outputFilled = getSlot(OUT).getHasStack();

            pendingCount = 0;
            if (!outputFilled && !inputFilled)
            {
                state = State.IDLE;
            }
            else if (!outputFilled && inputFilled)
            {
                state = State.COMPOSING;
            }
            else if (outputFilled && !inputFilled)
            {
                state = State.DECOMPOSING;
                ((SlotSeqProgrammerResult) getSlot(OUT)).lock = getSlot(OUT).getStack();
            }
            else
            {
                state = State.ERROR;
            }
            updateLastInv();

            if (state != State.IDLE && state != State.ERROR)
                updateCrafting();
        }

        updateLastInv();

        needCraftingUpdate = true;
    }

    /* ------------------------------------------------------------ */

    private void updateLastInv()
    {
        for (int i = 0; i < lastInv.length; i++)
            lastInv[i] = getSlot(i).getHasStack() ? getSlot(i).getStack().copy() : null;
    }

    /* ------------------------------------------------------------ */

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack())
            return null;

        ItemStack stack = slot.getStack();
        ItemStack original = stack.copy();

        int start = invInput.getSizeInventory() + invOutput.getSizeInventory();
        int end = start + player.inventory.mainInventory.length;
        if (slotIndex < OUT) // inputSlot clicked -> merge to player
        {
            @SuppressWarnings("unchecked")
            List<Slot> slots = this.inventorySlots;
            if (!Utils.mergeItemStack(slots, stack, start, end, true))
                return null;
        }
        else if (slotIndex == OUT) // outputSlot clicked -> merge to player TODO: Merge into inputSlot first
        {
            @SuppressWarnings("unchecked")
            List<Slot> slots = this.inventorySlots;
            if (!Utils.mergeItemStack(slots, stack, start, end, true))
                return null;
        }
        else if (slotIndex > OUT) // item from player inventory -> merge to first fitting Slot
        {
            @SuppressWarnings("unchecked")
            List<Slot> slots = this.inventorySlots;
            if (!Utils.mergeItemStack(slots, stack, 0, 3, false))
                return null;
        }

        if (stack.stackSize == 0)
            slot.putStack((ItemStack) null);
        else
            slot.onSlotChanged();

        if (stack.stackSize == original.stackSize)
            return null;
        slot.onPickupFromSlot(player, stack);

        return original;
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        for (int i = 0; i < invInput.getSizeInventory(); ++i)
        {
            ItemStack stack = invInput.getStackInSlotOnClosing(i);
            if (stack != null)
            {
                player.dropPlayerItemWithRandomChoice(stack, false);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }

}
