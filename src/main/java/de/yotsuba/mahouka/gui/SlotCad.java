package de.yotsuba.mahouka.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCad extends Slot
{

    private final CadProgrammerContainer container;

    public SlotCad(CadProgrammerContainer container, IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.container = container;
    }

    @Override
    public void onSlotChanged()
    {
        super.onSlotChanged();
        if (getStack() != null)
            container.cadToSequences();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
    {
        container.sequencesToCad(stack);
        super.onPickupFromSlot(player, stack);
    }

}
