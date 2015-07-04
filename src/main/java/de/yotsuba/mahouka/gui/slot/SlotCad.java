package de.yotsuba.mahouka.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.gui.container.CadProgrammerContainer;

public class SlotCad extends Slot
{

    @SuppressWarnings("unused")
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
        container.onCadChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
    {
        super.onPickupFromSlot(player, stack);
        container.onCadChanged();
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

}
