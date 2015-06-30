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
        // craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
        craftResult.setInventorySlotContents(0, ProcessAssembler.combine(craftMatrix.getStackInSlot(0), craftMatrix.getStackInSlot(1)));
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        if (!this.worldObj.isRemote)
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
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        Slot slotObject = (Slot) inventorySlots.get(slot);

        // null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject == null || !slotObject.getHasStack())
            return null;

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
        // return stack;

        ItemStack stack = null;
        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack itemstack1 = slotObject.getStack();
            stack = itemstack1.copy();

            if (slot == 0)
            {
                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                    return null;
                slotObject.onSlotChange(itemstack1, stack);
            }
            else if (slot >= 10 && slot < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                    return null;
            }
            else if (slot >= 37 && slot < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                    return null;
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
                slotObject.putStack((ItemStack) null);
            else
                slotObject.onSlotChanged();

            if (itemstack1.stackSize == stack.stackSize)
                return null;
            slotObject.onPickupFromSlot(player, itemstack1);
        }
        return stack;
    }

    @Override
    public boolean func_94530_a(ItemStack stack, Slot slot)
    {
        return slot.inventory != craftResult && super.func_94530_a(stack, slot);
    }

}
