package de.yotsuba.mahouka.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.cad.CadBase;

public class CadManager
{

    public static Map<UUID, CadBase> cads = new HashMap<UUID, CadBase>();

    /* ------------------------------------------------------------ */

    public static void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        // TODO: Currently we cache CAD data until server shutdown - should be changed sometime
        cads.clear();
    }

    public static CadBase getCad(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
            return null;
        CadBase cad = cads.get(UUID.fromString(tag.getString("id")));
        if (cad == null)
        {
            cad = ((ItemCad) stack.getItem()).createNewCad();
            cad.readFromNBT(tag);
            cads.put(cad.getId(), cad);
        }
        else if (tag.getBoolean("changed"))
            cad.readFromNBT(tag);
        return cad;
    }

    public static CadBase getCad(UUID id)
    {
        return cads.get(id);
    }

}
