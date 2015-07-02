package de.yotsuba.mahouka.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.gui.container.ProcessProgrammerContainer;
import de.yotsuba.mahouka.item.ItemMagicProcess;

public class SlotProcess extends Slot
{

    private final ProcessProgrammerContainer container;

    public SlotProcess(ProcessProgrammerContainer container, IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.container = container;
    }

    @Override
    public void onSlotChanged()
    {
        super.onSlotChanged();
        container.processChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
    {
        super.onPickupFromSlot(player, stack);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() instanceof ItemMagicProcess;
    }

}
