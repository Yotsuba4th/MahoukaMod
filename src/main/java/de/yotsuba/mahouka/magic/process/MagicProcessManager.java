package de.yotsuba.mahouka.magic.process;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class MagicProcessManager
{

    public static Map<Short, Class<? extends MagicProcess>> processTypes = new HashMap<Short, Class<? extends MagicProcess>>();

    public static Map<Class<? extends MagicProcess>, Short> processIds = new HashMap<Class<? extends MagicProcess>, Short>();

    static
    {
        registerProcess(ProcessParticle.class, (short) 1);
        registerProcess(ProcessExplosion.class, (short) 2);
    }

    public static MagicProcess readFromNBT(NBTTagCompound tag)
    {
        Class<? extends MagicProcess> clazz = processTypes.get(tag.getShort("id"));
        if (clazz == null)
        {
            // TODO: Print error
            return null;
        }

        try
        {
            MagicProcess process = clazz.newInstance();
            process.readFromNBT(tag);
            return process;
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerProcess(Class<? extends MagicProcess> clazz, short id)
    {
        if (processTypes.containsKey(id))
            throw new RuntimeException(String.format("Duplicate assignment of magic process id %d", id));
        processTypes.put(id, clazz);
        processIds.put(clazz, id);
    }

    public static short getId(Class<? extends MagicProcess> clazz)
    {
        Short id = processIds.get(clazz);
        if (id == null)
            throw new RuntimeException(String.format("Magic process class %s has not been registered", clazz.toString()));
        return id;
    }

}
