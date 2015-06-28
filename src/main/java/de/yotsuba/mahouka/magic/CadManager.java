package de.yotsuba.mahouka.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.cad.CadBase;

public class CadManager
{

    public static Map<UUID, CadBase> cads = new HashMap<UUID, CadBase>();

    /* ------------------------------------------------------------ */

    public CadManager()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    public void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        // TODO: Currently we cache CAD data until server shutdown - should be changed sometime
        cads.clear();
    }

    public CadBase getCad(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
            return null;
        CadBase cad = cads.get(tag.getString("id"));
        if (cad == null)
        {
            cad = ((ItemCad) stack.getItem()).createNewCad();
            cads.put(cad.getId(), cad);
            cad.readFromNBT(tag);
        }
        else if (tag.getBoolean("changed"))
            cad.readFromNBT(tag);
        return cad;
    }

}
