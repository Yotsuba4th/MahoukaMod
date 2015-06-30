package de.yotsuba.mahouka.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IIconRegister;
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

    public static final String DEFAULT_ICON = MahoukaMod.MODID + ":magic_process";

    public static Map<String, IIcon> icons = new HashMap<String, IIcon>();

    public static void registerIcon(String textureName)
    {
        icons.put(textureName, null);
    }

    /* ------------------------------------------------------------ */

    public ItemMagicSequence()
    {
        setFull3D();
        setUnlocalizedName("magic_sequence");
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
    public void registerIcons(IIconRegister iconReg)
    {
        for (Entry<String, IIcon> icon : icons.entrySet())
            icon.setValue(iconReg.registerIcon(icon.getKey()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack)
    {
        IIcon icon = icons.get(getTextureName(stack));
        if (icon == null)
            icon = icons.get(DEFAULT_ICON);
        return icon;
    }

    public String getTextureName(ItemStack stack)
    {
        ActivationSequence sequence = getSequence(stack);
        if (sequence == null)
            return DEFAULT_ICON;
        return sequence.getTextureName();
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
