package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.core.PlayerData;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.cast.CastingManager;
import de.yotsuba.mahouka.network.S1StartChanneling;
import de.yotsuba.mahouka.network.S4CancelCast;
import de.yotsuba.mahouka.util.WorldUtils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetPoint;

public class CadBase extends InventoryBasic
{

    public static final String NBT_SEQUENCES = "seq";

    private UUID id;

    private ActivationSequence[] activationSequences = new ActivationSequence[1];

    private ItemStack[] items = new ItemStack[1];

    private byte selectedSequence;

    /* ------------------------------------------------------------ */

    public CadBase(int size)
    {
        super(null, false, size);
        id = UUID.randomUUID();
    }

    /* ------------------------------------------------------------ */

    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setBoolean("changed", true);
        tag.setString("id", id.toString());
        tag.setByte("sel", selectedSequence);

        // Write sequences
        NBTTagList tagSequences = new NBTTagList();
        tag.setTag(NBT_SEQUENCES, tagSequences);
        for (int i = 0; i < activationSequences.length; i++)
        {
            if (activationSequences[i] == null)
                continue;
            NBTTagCompound tagSequence = new NBTTagCompound();
            items[i].writeToNBT(tagSequence);
            tagSequence.setByte("idx", (byte) i);
            tagSequences.appendTag(tagSequence);
        }

    }

    public void readFromNBT(NBTTagCompound tag)
    {
        id = UUID.fromString(tag.getString("id"));
        selectedSequence = tag.getByte("sel");

        // Read sequences
        NBTTagList tagSequences = tag.getTagList(NBT_SEQUENCES, 10);
        for (int i = 0; i < activationSequences.length; i++)
        {
            activationSequences[i] = null;
            items[i] = null;
        }
        for (int i = 0; i < tagSequences.tagCount(); i++)
        {
            NBTTagCompound tagSequence = tagSequences.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(tagSequence);
            if (!(stack.getItem() instanceof ItemMagicSequence))
                continue;
            ItemMagicSequence item = (ItemMagicSequence) stack.getItem();
            items[tagSequence.getByte("idx")] = stack;
            activationSequences[tagSequence.getByte("idx")] = new ActivationSequence(item.getStackData(stack));
        }
    }

    public void readFromItems()
    {
        // TODO Auto-generated method stub

    }

    /* ------------------------------------------------------------ */

    public void rightClick(ItemStack stack, EntityPlayer player)
    {
        if (!player.worldObj.isRemote)
            return;
        if (CastingManager.isServerCasting(id))
        {
            if (player.isSneaking())
                S4CancelCast.send(id);
            else
                player.addChatMessage(new ChatComponentText("Another magic is still active!"));
        }
        else
        {
            ActivationSequence sequence = getSelectedSequence();
            if (sequence == null)
            {
                // TODO (4) Error sound
                player.addChatMessage(new ChatComponentText("No sequence selected!"));
                return;
            }

            PlayerData playerData = new PlayerData(player);
            if (playerData.getPsion() < 10)
            {
                // TODO (4) Error sound
                player.addChatMessage(new ChatComponentText("Not enough psion!"));
                return;
            }

            Target target = player.isSneaking() ? new TargetEntity(player, true, false) : selectTarget(player);
            if (target == null)
            {
                // TODO (4) Error sound
                player.addChatMessage(new ChatComponentText("No target selected!"));
                return;
            }

            if (sequence.getProcesses().isEmpty())
            {
                // TODO (4) Error sound
                player.addChatMessage(new ChatComponentText("Empty sequence in CAD!"));
                return;
            }
            if (!sequence.getProcesses().get(0).isTargetValid(target))
            {
                target = new TargetEntity(player, true, false);
                if (!sequence.getProcesses().get(0).isTargetValid(target))
                {
                    // TODO (4) Error sound
                    player.addChatMessage(new ChatComponentText("Invalid target!"));
                    return;
                }
            }

            S1StartChanneling.send(player, id, target);
        }
        updateItemStack(stack, player);
    }

    public void updateItemStack(ItemStack stack, EntityPlayer player)
    {
        PlayerData playerData = new PlayerData(player);
        stack.setItemDamage(stack.getItem().getMaxDamage() - 1 - playerData.getPsion() * stack.getItem().getMaxDamage() / playerData.getMaxPsion());
        writeToNBT(stack.getTagCompound());
    }

    @SideOnly(Side.CLIENT)
    public Target selectTarget(EntityPlayer player)
    {
        MovingObjectPosition result = WorldUtils.rayTraceTarget(16 * 8);
        if (result.typeOfHit == MovingObjectType.ENTITY)
            return new TargetEntity(result.entityHit, false, false);
        if (result.typeOfHit == MovingObjectType.BLOCK)
            return new TargetBlock(player.worldObj, result.blockX, result.blockY, result.blockZ, result.hitVec);
        return new TargetPoint(result.hitVec);
    }

    /* ------------------------------------------------------------ */

    public UUID getId()
    {
        return id;
    }

    public ActivationSequence[] getActivationSequences()
    {
        return activationSequences;
    }

    public int getSelectedSequenceIndex()
    {
        return selectedSequence;
    }

    public void setSelectedSequenceIndex(byte index)
    {
        if (CastingManager.isServerCasting(id))
            return;
        if (index < 0)
            selectedSequence = (byte) (activationSequences.length - 1);
        else if (index >= activationSequences.length)
            selectedSequence = 0;
        else
            selectedSequence = index;
    }

    public ActivationSequence getSelectedSequence()
    {
        return activationSequences[selectedSequence];
    }

    /* ------------------------------------------------------------ */

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= getSizeInventory())
            return;
        super.setInventorySlotContents(index, stack);
    }

}
