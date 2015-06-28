package de.yotsuba.mahouka.magic.cad;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicSequence;
import de.yotsuba.mahouka.magic.Target;

public class CadBase
{

    private String id;

    public CadBase()
    {
        id = UUID.randomUUID().toString();
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setString("id", id);
        tag.setBoolean("changed", true);
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        id = tag.getString("id");
    }

    public void rightClick(ItemStack stack, EntityPlayer player)
    {
        if (stack.getItemDamage() >= stack.getMaxDamage())
        {
            // TODO: Play error sound
            return;
        }

        // TODO: Use cad
        stack.setItemDamage(50);

        // if (!player.capabilities.isCreativeMode)
    }

    public MagicSequence startChanneling(Target target, ActivationSequence sequence)
    {
        return null;
    }

    public void channelComplete(MagicSequence cast, ActivationSequence sequence)
    {
    }

    public String getId()
    {
        return id;
    }

}
