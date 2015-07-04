package de.yotsuba.mahouka.magic.cad;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.yotsuba.mahouka.item.ItemCad;

public class CadManager
{

    public static Map<ItemStack, CadBase> cads = new HashMap<ItemStack, CadBase>();

    /* ------------------------------------------------------------ */

    public static void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        // TODO (6) Currently we cache CAD data until server shutdown - should be changed sometime
        cads.clear();
    }

    public static CadBase getCad(ItemStack stack)
    {
        if (!(stack.getItem() instanceof ItemCad))
            return null;
        CadBase cad = cads.get(stack);
        NBTTagCompound tag = stack.getTagCompound();
        if (cad == null)
        {
            cad = ((ItemCad) stack.getItem()).createNewCad();
            cads.put(stack, cad);
            if (tag == null)
            {
                tag = new NBTTagCompound();
                cad.writeToNBT(tag);
                stack.setTagCompound(tag);
            }
            else
                cad.readFromNBT(tag);
        }
        if (tag != null && tag.getBoolean("changed"))
            cad.readFromNBT(tag);
        return cad;
    }

}
