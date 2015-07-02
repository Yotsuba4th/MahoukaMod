package de.yotsuba.mahouka.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.MagicProcess;

public class ItemMagicProcess extends ItemMagicSequence
{

    private final MagicProcess process;

    public ItemMagicProcess(MagicProcess process)
    {
        super();
        this.process = process;
        setCreativeTab(MahoukaMod.creativeTab);
        setUnlocalizedName(process.getItemName());
    }

    /* ------------------------------------------------------------ */

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        getStackData(stack);
    }

    @Override
    public NBTTagCompound getStackData(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null)
            return tag;

        tag = new NBTTagCompound();
        stack.setTagCompound(tag);

        NBTTagList list = tag.getTagList(ActivationSequence.NBT_PROCESSES, 10);
        tag.setTag(ActivationSequence.NBT_PROCESSES, list);

        list.appendTag(process.writeToNBT());
        return tag;
    }

    public MagicProcess getItemProcess()
    {
        return process;
    }
    
    public MagicProcess getProcess(ItemStack stack)
    {
        NBTTagCompound tag = getStackData(stack);
        if (tag == null)
            return null;
        ActivationSequence sequence = new ActivationSequence();
        sequence.readFromNBT(getStackData(stack));
        if (sequence.getProcesses().size() != 1)
            throw new RuntimeException("Cannot get process of magic sequence!");
        return sequence.getProcesses().get(0);
    }


    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack)
    {
        IIcon icon = icons.get(process.getTextureName());
        if (icon != null)
            return icon;
        return super.getIconIndex(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegistry)
    {
        /* do nothing */
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

    public void drawGuiParts()
    {
        /* do nothing */
    }

}
