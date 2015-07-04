package de.yotsuba.mahouka.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.gui.container.CadProgrammerContainer;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.cad.CadBase;

public class SlotSequence extends Slot
{

    protected final CadProgrammerContainer container;

    public SlotSequence(CadProgrammerContainer container, int index, int x, int y)
    {
        super(null, index, x, y);
        this.container = container;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        // TODO (2) Return false if slot out of CAD size
        return stack.getItem() instanceof ItemMagicSequence;
    }

    @Override
    public ItemStack getStack()
    {
        if (container.getCad() == null)
            return null;
        ItemStack stack = container.getCad().getStackInSlot(getSlotIndex());
        return stack;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    @Override
    public void putStack(ItemStack stack)
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return;
        cad.setInventorySlotContents(getSlotIndex(), stack);
        cad.writeToNBT(container.getSlot(0).getStack().getTagCompound());
        onSlotChanged();
    }

    @Override
    public void onSlotChanged()
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return;
        cad.markDirty();
    }

    @Override
    public int getSlotStackLimit()
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return 0;
        return cad.getInventoryStackLimit();
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return null;
        return cad.decrStackSize(getSlotIndex(), amount);
    }

    /**
     * returns true if this slot is in par2 of par1
     */
    @Override
    public boolean isSlotInInventory(IInventory inv, int index)
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return false;
        return inv == cad && index == getSlotIndex();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getBackgroundIconIndex()
    {
        // TODO (2) Return blocked background icon if slot out of CAD size
        return backgroundIcon;
    }

}
