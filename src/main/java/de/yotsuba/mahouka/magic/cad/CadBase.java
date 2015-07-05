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
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.magic.MagicProcess;
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

    /* ------------------------------------------------------------ */

    private UUID id;

    private final MagicProcess[] activationSequences;

    private byte selectedSequence;

    /* ------------------------------------------------------------ */

    public CadBase(int size)
    {
        super(null, false, size);
        activationSequences = new MagicProcess[size];
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
            ItemStack stack = getStackInSlot(i);
            if (stack != null)
            {
                if (activationSequences[i] != null)
                    activationSequences[i].writeToNBT(stack.getTagCompound());
                NBTTagCompound tagSequence = new NBTTagCompound();
                stack.writeToNBT(tagSequence);
                tagSequence.setByte("idx", (byte) i);
                tagSequences.appendTag(tagSequence);
            }
        }

    }

    public void readFromNBT(NBTTagCompound tag)
    {
        tag.setBoolean("changed", false);
        id = UUID.fromString(tag.getString("id"));
        selectedSequence = tag.getByte("sel");

        // Read sequences
        NBTTagList tagSequences = tag.getTagList(NBT_SEQUENCES, 10);
        for (int i = 0; i < activationSequences.length; i++)
        {
            activationSequences[i] = null;
            setInventorySlotContents(i, null);
        }
        for (int i = 0; i < tagSequences.tagCount(); i++)
        {
            NBTTagCompound tagSequence = tagSequences.getCompoundTagAt(i);
            if (!tagSequence.hasKey("idx"))
                continue;
            int idx = tagSequence.getByte("idx");

            ItemStack stack = ItemStack.loadItemStackFromNBT(tagSequence);
            if (!(stack.getItem() instanceof ItemMagicProcess))
                continue;
            setInventorySlotContents(idx, stack);

            ItemMagicProcess item = (ItemMagicProcess) stack.getItem();
            MagicProcess process = item.getItemProcess().copy();
            process.readFromNBT(item.getStackData(stack));
            activationSequences[idx] = process;
        }
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
            MagicProcess sequence = getSelectedSequence();
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

            if (!sequence.isTargetValid(target))
            {
                target = new TargetEntity(player, true, false);
                if (!sequence.isTargetValid(target))
                {
                    // TODO (4) Error sound
                    player.addChatMessage(new ChatComponentText("Invalid target!"));
                    return;
                }
            }

            S1StartChanneling.send(target);
        }
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

    public MagicProcess[] getActivationSequences()
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

    public MagicProcess getSelectedSequence()
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
