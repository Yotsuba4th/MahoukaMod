package de.yotsuba.mahouka.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.process.MagicProcess;

public class ItemMagicProcess extends Item
{
    public static String id = "magic_process";

    public final MagicProcess process;

    public ItemMagicProcess(MagicProcess process)
    {
        setFull3D();
        setUnlocalizedName(id);
        setCreativeTab(MahoukaMod.creativeTab);
        setTextureName(MahoukaMod.MODID + ":" + id);
        this.process = process;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean shiftPressed)
    {
        // if (process != null)
        // info.add("Magic Process: " + process.getClass().getName());
    }
}
