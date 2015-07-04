package de.yotsuba.mahouka.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.MagicProcess;

public class ItemMagicProcess extends Item
{

    private final MagicProcess process;

    public ItemMagicProcess(MagicProcess process)
    {
        this.process = process;
        setCreativeTab(MahoukaMod.creativeTab);
        setUnlocalizedName(process.getItemName());
        setTextureName(process.getTextureName());
    }

    /* ------------------------------------------------------------ */

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        getStackData(stack);
    }

    public MagicProcess getItemProcess()
    {
        return process;
    }

    public MagicProcess getProcess(ItemStack stack)
    {
        if (stack.getTagCompound() == null)
            return process;
        MagicProcess proc = process.copy();
        proc.readFromNBT(stack.getTagCompound());
        return proc;
    }

    public NBTTagCompound getStackData(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
        {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
            process.writeToNBT(tag);
        }
        return tag;
    }

    /* ------------------------------------------------------------ */

    @Override
    public String getUnlocalizedName()
    {
        return "mahouka.process." + process.getName();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return getUnlocalizedNameInefficiently(stack);
        // return getUnlocalizedNameInefficiently(stack) + " (Psi " + process.getPsionCost() + ")";
    }

    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean bool1)
    {
        getProcess(stack).addInformation(info, true);
    }

}
