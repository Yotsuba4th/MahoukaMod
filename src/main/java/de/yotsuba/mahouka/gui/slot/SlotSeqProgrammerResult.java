package de.yotsuba.mahouka.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.gui.container.SequenceProgrammerContainer;
import de.yotsuba.mahouka.gui.container.SequenceProgrammerContainer.State;
import de.yotsuba.mahouka.item.ItemMagicProcess;

public class SlotSeqProgrammerResult extends Slot
{

    private final SequenceProgrammerContainer container;

    public SlotSeqProgrammerResult(SequenceProgrammerContainer container, IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.container = container;
    }

    public ItemStack lock;

    @Override
    public void onSlotChanged()
    {
        if (container.needCraftingUpdate)
        {
            container.updateCrafting();
        }
        super.onSlotChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
    {
        container.needCraftingUpdate = false;
        switch (container.state)
        {
        case DECOMPOSING:
            if (inventory == container.invInput && container.getSlot(SequenceProgrammerContainer.OUT).getHasStack() && stack.stackSize > 1)
            {
                putStack(stack.splitStack(1));
                container.pendingCount = 0;
            }
        default:
            break;
        }
        container.needCraftingUpdate = true;
        super.onPickupFromSlot(player, stack);
    }

    @Override
    public void putStack(ItemStack stack)
    {
        if (isItemValid(stack))
            super.putStack(stack);
    }

    @Override
    public int getSlotStackLimit()
    {
        if (container.state == State.IDLE //
                || (container.state == State.COMPOSING && inventory == container.invInput)
                || (container.state == State.DECOMPOSING && inventory == container.invOutput))
            return 64;
        return 0;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if (stack == null)
        {
            lock = stack;
            return true;
        }
        if (container.pendingCount > 0 && lock != null)
            return ItemStack.areItemStackTagsEqual(stack, lock);
        return stack.getItem() instanceof ItemMagicProcess;
    }

}
