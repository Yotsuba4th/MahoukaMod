package de.yotsuba.mahouka.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.yotsuba.mahouka.gui.slot.SlotSeqProgrammerResult;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.ProcessAssembler;
import de.yotsuba.mahouka.util.Utils;

public class SequenceProgrammerContainer extends Container implements IInventory
{

    public static final int IN_1 = 0;

    public static final int IN_2 = 1;

    public static final int OUT = 2;

    /* ------------------------------------------------------------ */

    private final World worldObj;

    public ItemStack[] inventory = new ItemStack[3];

    /* ------------------------------------------------------------ */

    public SequenceProgrammerContainer(InventoryPlayer playerInventory)
    {
        worldObj = playerInventory.player.worldObj;
        addSlotToContainer(new Slot(this, 0, 49, 18));
        addSlotToContainer(new Slot(this, 1, 49, 54));
        addSlotToContainer(new SlotSeqProgrammerResult(this, OUT, 107, 36));
        for (Slot slot : Utils.getPlayerContainerSlots(playerInventory))
            addSlotToContainer(slot);
    }

    /* ------------------------------------------------------------ */

    private void updateCrafting(int slot, boolean inserted)
    {
        if (inserted)
        {
            if (slot == OUT)
            {
                ItemStack output1 = getStackInSlot(OUT);
                if (output1 != null)
                {
                    output1 = output1.copy();
                    ItemStack output2 = ProcessAssembler.split(output1);
                    inventory[IN_1] = ProcessAssembler.convertToProcess(output1);
                    inventory[IN_2] = output2;
                    if (output2 == null)
                        inventory[OUT] = null;
                    else
                        inventory[OUT].stackSize = 1;
                }
            }
            else
            {
                ItemStack input1 = getStackInSlot(IN_1);
                ItemStack input2 = getStackInSlot(IN_2);
                inventory[OUT] = ProcessAssembler.combine(input1, input2);
            }
        }
        else
        {
            updateOutputCount();
            if (slot == OUT)
            {
                if (inventory[IN_1] != null && inventory[IN_2] != null)
                {
                    for (int i = IN_1; i <= IN_2; i++)
                    {
                        ItemStack stack = inventory[i];
                        stack = stack.splitStack(1);
                        if (inventory[i].stackSize == 0)
                            inventory[i] = null;
                    }
                }
                updateCrafting(IN_1, true);
            }
            else
            {
                if (slot == IN_2 && inventory[IN_2] == null)
                {
                    inventory[OUT] = inventory[IN_1];
                    inventory[IN_1] = null;
                    updateCrafting(OUT, true);
                }
                else if (slot == IN_1 && inventory[IN_1] == null)
                {
                    inventory[OUT] = null;
                }
            }
        }
    }

    public void updateOutputCount()
    {
        if (inventory[OUT] != null)
        {
            if (inventory[IN_1] != null)
                inventory[IN_1].stackSize += inventory[OUT].stackSize - 1;
            if (inventory[IN_2] != null)
                inventory[IN_2].stackSize += inventory[OUT].stackSize - 1;
            inventory[OUT].stackSize = 1;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        if (worldObj.isRemote)
            return;
        updateOutputCount();
        for (int i = 0; i < 2; ++i)
        {
            ItemStack stack = getStackInSlotOnClosing(i);
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
        // return worldObj.getBlock(posX, posY, posZ) != Blocks.crafting_table ? false :
        // p_75145_1_.getDistanceSq((double) posX + 0.5D, (double) posY + 0.5D,
        // (double) posZ + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
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
        return false;
        // return slot.inventory != craftResult && super.func_94530_a(stack, slot);
    }

    /* ------------------------------------------------------------ */

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if (inventory[slot] == null)
            return null;
        updateOutputCount();
        ItemStack stack = inventory[slot];
        if (stack.stackSize <= amount)
        {
            inventory[slot] = null;
            updateCrafting(slot, false);
            return stack;
        }
        else
        {
            stack = stack.splitStack(amount);
            if (inventory[slot].stackSize == 0)
                inventory[slot] = null;
            updateCrafting(slot, false);
            return stack;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (inventory[slot] != null)
        {
            // TODO: This will drop duplicates currently!!!!
            ItemStack itemstack = inventory[slot];
            inventory[slot] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
        updateCrafting(slot, stack != null);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return stack.getItem() instanceof ItemMagicSequence;
    }

    @Override
    public String getInventoryName()
    {
        return "container.crafting";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
    }

}
