package de.yotsuba.mahouka.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.cad.CadManager;

public class ItemCad extends Item
{

    public ItemCad()
    {
        maxStackSize = 1;
        setFull3D();
        setUnlocalizedName("cad");
        setTextureName(MahoukaMod.MODID + ":cad");
        setCreativeTab(MahoukaMod.creativeTab);
    }

    public CadBase createNewCad()
    {
        return new CadBase();
    }

    @Override
    public int getMaxDamage()
    {
        // Constant 100 max damage - stack damage will be set individually based on CAD data
        return 100;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        CadBase cad = CadManager.getCad(stack);
        if (cad == null)
        {
            if (world.isRemote)
            {
                // TODO: Play error sound
                player.addChatMessage(new ChatComponentText("No sequence selected"));
            }
        }
        else
        {
            cad.rightClick(stack, player);
        }
        return stack;
    }

    /**
     * Add custom information when hovering the item
     */
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean shiftPressed)
    {
        CadBase cad = CadManager.getCad(stack);
        if (cad != null)
        {
            info.add("Cad instance: " + cad.getId().toString().substring(0, 6));
        }
    }

}
