package de.yotsuba.mahouka.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.process.MagicProcess;

public class ItemMagicProcess extends ItemMagicSequence
{

    private final MagicProcess process;

    public ItemMagicProcess(MagicProcess process)
    {
        super();
        setFull3D();
        setCreativeTab(MahoukaMod.creativeTab);
        this.process = process;
        ItemMagicSequence.registerIcon(process.getTextureName());
    }

    public MagicProcess getItemProcess()
    {
        return process;
    }

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
        tag = process.writeToNBT();
        stack.setTagCompound(tag);
        return tag;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack)
    {
        return icons.get(process.getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconReg)
    {
    }

}
