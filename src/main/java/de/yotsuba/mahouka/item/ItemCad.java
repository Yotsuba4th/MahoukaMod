package de.yotsuba.mahouka.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.ActivationSequence;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.process.ProcessParticle;

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

    public CadBase getCad(ItemStack stack)
    {
        return MahoukaMod.getCadManager().getCad(stack);
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        CadBase cad = getCad(stack);

        // TODO: TEST KOT
        if (cad == null && !world.isRemote)
        {
            cad = createNewCad();
            
            ActivationSequence seq = new ActivationSequence();
            seq.getProcesses().add(new ProcessParticle());
            cad.getActivationSequences()[0] = seq;
            
            if (stack.getTagCompound() == null)
                stack.setTagCompound(new NBTTagCompound());
            cad.writeToNBT(stack.getTagCompound());
        }

        if (cad == null)
        {
            if (world.isRemote)
            {
                // TODO: Play error sound
                player.addChatMessage(new ChatComponentText("CAD not initialized"));
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
        CadBase cad = getCad(stack);
        if (cad != null)
        {
            info.add("Cad instance: " + cad.getId().substring(0, 6));
        }
    }

}
