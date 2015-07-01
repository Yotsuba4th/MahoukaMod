package de.yotsuba.mahouka.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.ActivationSequence;

public class ItemMagicSequence extends Item
{

    public ItemMagicSequence()
    {
        setFull3D();
        setUnlocalizedName("magic_sequence");
        setTextureName(MahoukaMod.MODID + ":magic_sequence");
    }

    /* ------------------------------------------------------------ */

    public NBTTagCompound getStackData(ItemStack stack)
    {
        return stack.getTagCompound();
    }

    public ActivationSequence getSequence(ItemStack stack)
    {
        NBTTagCompound tag = getStackData(stack);
        if (tag == null)
            return null;
        ActivationSequence sequence = new ActivationSequence();
        sequence.readFromNBT(getStackData(stack));
        return sequence;
    }

    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack)
    {
        ActivationSequence sequence = getSequence(stack);
        if (sequence != null)
        {
            IIcon icon = sequence.getIcon();
            if (icon != null)
                return icon;
        }
        return super.getIconIndex(stack);
    }

    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean bool1)
    {
        ActivationSequence sequence = getSequence(stack);
        if (sequence == null)
            return;
        sequence.addInformation(info);
    }

}
