package de.yotsuba.mahouka.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.gui.container.CadProgrammerContainer;
import de.yotsuba.mahouka.item.ItemMagicProcess;
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
        if (!(stack.getItem() instanceof ItemMagicProcess))
            return false;
        CadBase cad = container.getCad();
        if (cad == null)
            return false;
        return getSlotIndex() < cad.getSizeInventory();
    }

    @Override
    public ItemStack getStack()
    {
        if (container.getCad() == null)
            return null;
        ItemStack stack = container.getCad().getStackInSlot(getSlotIndex());
        return stack;
    }

    @Override
    public void putStack(ItemStack stack)
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return;
        cad.setInventorySlotContents(getSlotIndex(), stack);
        onSlotChanged();
    }

    @Override
    public void onSlotChanged()
    {
        CadBase cad = container.getCad();
        if (cad == null)
            return;
        cad.markDirty();
        cad.writeToNBT(container.getSlot(0).getStack().getTagCompound());
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
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
