package de.yotsuba.mahouka.gui.container;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import de.yotsuba.mahouka.util.Utils;

public abstract class ContainerExt extends Container
{

    @Override
    @SuppressWarnings("unchecked")
    protected boolean mergeItemStack(ItemStack stack, int fromSlot, int toSlot, boolean backward)
    {
        return Utils.mergeItemStack(this.inventorySlots, stack, fromSlot, toSlot, backward);
    }

}
