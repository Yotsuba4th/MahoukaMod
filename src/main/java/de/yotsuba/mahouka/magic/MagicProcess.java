package de.yotsuba.mahouka.magic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.magic.process.ProcessExplosion;
import de.yotsuba.mahouka.magic.process.ProcessParticle;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public abstract class MagicProcess implements Cloneable
{

    public static Map<Short, Class<? extends MagicProcess>> processTypes = new HashMap<Short, Class<? extends MagicProcess>>();

    public static Map<Class<? extends MagicProcess>, Short> processIds = new HashMap<Class<? extends MagicProcess>, Short>();

    static
    {
        registerProcess(ProcessParticle.class, (short) 1);
        registerProcess(ProcessExplosion.class, (short) 2);
    }

    /* ------------------------------------------------------------ */

    public static void registerProcess(Class<? extends MagicProcess> clazz, short id)
    {
        if (processTypes.containsKey(id))
            throw new RuntimeException(String.format("Duplicate assignment of magic process id %d", id));
        processTypes.put(id, clazz);
        processIds.put(clazz, id);
    }

    public static MagicProcess createFromNBT(NBTTagCompound tag)
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

    /* ------------------------------------------------------------ */

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("id", getId());
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
    }

    public short getId()
    {
        Short id = processIds.get(getClass());
        if (id == null)
            throw new RuntimeException(String.format("Magic process class %s has not been registered", getClass().toString()));
        return id;
    }

    /* ------------------------------------------------------------ */

    public MagicProcess copy()
    {
        try
        {
            return (MagicProcess) this.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException("Unexpected error");
        }
    }

    /* ------------------------------------------------------------ */

    public abstract TargetType[] getValidTargets();

    public abstract int getChannelingDuration();

    public abstract int getCastDuration(Target target);

    /* ------------------------------------------------------------ */

    public String getTextureName()
    {
        return ItemMagicSequence.DEFAULT_ICON;
    }

    public boolean isContinuousCast(Target target)
    {
        return getCastDuration(target) > 0;
    }

    public void addInformation(List<String> info, boolean isSequence)
    {
        info.add(getClass().getSimpleName());
    }

    /* ------------------------------------------------------------ */

    public Target cast(CastingProcess cp, Target target)
    {
        return target;
    }

    @SideOnly(Side.CLIENT)
    public void castClient(CastingProcess cp, Target target)
    {
        /* do nothing */
    }

    /* ------------------------------------------------------------ */

    /**
     * Called, if isContinuousCast is true
     * 
     * @param cad
     * @param target
     */
    public void castTick(CastingProcess cp, Target target)
    {
    }

    /**
     * Called on the client side, if isContinuousCast is true
     * 
     * @param cad
     * @param target
     */
    @SideOnly(Side.CLIENT)
    public void castTickClient(CastingProcess cp, Target target)
    {
        /* do nothing */
    }

}
