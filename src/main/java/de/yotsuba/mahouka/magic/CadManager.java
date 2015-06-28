package de.yotsuba.mahouka.magic;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.cad.CadBase;

public class CadManager
{

    public Map<String, CadBase> cads = new HashMap<String, CadBase>();

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
        CadBase cad = (tag == null) ? null : cads.get(tag.getString("id"));
        if (tag == null)
        {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        if (cad == null)
        {
            cad = ((ItemCad) stack.getItem()).createNewCad();
            cads.put(cad.getId(), cad);
            cad.writeToNBT(tag);
        }
        else
        {
            if (tag.getBoolean("changed"))
                cad.readFromNBT(tag);
        }
        return cad;
    }

}
