package de.yotsuba.mahouka.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.yotsuba.mahouka.magic.ProcessAssembler;

public class SequenceProgrammerContainer extends Container
{
    private final World worldObj;

    private final InventoryCrafting craftMatrix = new InventoryCrafting(this, 1, 2);

    private final IInventory craftResult = new InventoryCraftResult();

    public SequenceProgrammerContainer(InventoryPlayer playerInventory)
    {
        worldObj = playerInventory.player.worldObj;
        addSlotToContainer(new Slot(craftMatrix, 0, 49, 18));
        addSlotToContainer(new Slot(craftMatrix, 1, 49, 54));
        addSlotToContainer(new SlotCrafting(playerInventory.player, craftMatrix, craftResult, 0, 107, 36));
        // addSlotToContainer(new Slot(craftResult, 0, 107, 36));
        
        addPlayerInventoryToContainer(playerInventory);
    }

    private void addPlayerInventoryToContainer(InventoryPlayer playerInventory)
    {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public void onCraftMatrixChanged(IInventory playerInventory)
    {
        // ItemStack input1 = craftMatrix.getStackInSlot(0);
        // ItemStack input2 = craftMatrix.getStackInSlot(1);
        // ItemStack output = craftResult.getStackInSlot(0);
        // if (input1 != null && input2 != null && output == null)
        // {
        // craftResult.setInventorySlotContents(0, ProcessAssembler.combine(input1, input2));
        // }
        // if ((input1 != null || input2 != null) && output != null)
        // {
        // craftResult.setInventorySlotContents(0, null);
        // }
        // else if (input1 == null && input2 == null && output != null)
        // {
        // ItemStack output1 = output.copy();
        // ItemStack output2 = ProcessAssembler.split(output1);
        // craftMatrix.setInventorySlotContents(0, output1);
        // craftMatrix.setInventorySlotContents(1, output2);
        // }
        craftResult.setInventorySlotContents(0, ProcessAssembler.combine(craftMatrix.getStackInSlot(0), craftMatrix.getStackInSlot(1)));
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        if (!worldObj.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                ItemStack stack = craftMatrix.getStackInSlotOnClosing(i);
                if (stack != null)
                {
                    player.dropPlayerItemWithRandomChoice(stack, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
        // return worldObj.getBlock(posX, posY, posZ) != Blocks.crafting_table ? false : p_75145_1_.getDistanceSq((double) posX + 0.5D, (double) posY + 0.5D,
        // (double) posZ + 0.5D) <= 64.0D;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        // null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slot == null || !slot.getHasStack())
            return null;

        ItemStack stack = null;
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            stack = itemstack1.copy();

            if (slotIndex < 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, false))
                    return null;
            }
            else if (slotIndex == 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, true))
                    return null;
                slot.onSlotChange(itemstack1, stack);
            }
            else if (slotIndex >= 3 && slotIndex < 39)
            {
                if (!mergeItemStack(itemstack1, 0, 2, false))
                    return null;
            }

            if (itemstack1.stackSize == 0)
                slot.putStack((ItemStack) null);
            else
                slot.onSlotChanged();

            if (itemstack1.stackSize == stack.stackSize)
                return null;
            slot.onPickupFromSlot(player, itemstack1);
        }
        return stack;
    }

    @Override
    public boolean func_94530_a(ItemStack stack, Slot slot)
    {
        return slot.inventory != craftResult && super.func_94530_a(stack, slot);
    }

}
