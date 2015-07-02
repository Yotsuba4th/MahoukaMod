package de.yotsuba.mahouka.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.gui.GuiContainerExt;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.magic.process.ProcessAccelerate;
import de.yotsuba.mahouka.magic.process.ProcessExplosion;
import de.yotsuba.mahouka.magic.process.ProcessFireShockwave;
import de.yotsuba.mahouka.magic.process.ProcessFireball;
import de.yotsuba.mahouka.magic.process.ProcessFirebomb;
import de.yotsuba.mahouka.magic.process.ProcessMovingOffset;
import de.yotsuba.mahouka.magic.process.ProcessOffset;
import de.yotsuba.mahouka.magic.process.ProcessParallel;
import de.yotsuba.mahouka.magic.process.ProcessParticle;
import de.yotsuba.mahouka.magic.process.ProcessShockwave;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public abstract class MagicProcess implements Cloneable
{

    public static final String DEFAULT_ICON = MahoukaMod.MODID + ":process_default_blue";

    /* ------------------------------------------------------------ */

    public static Map<Short, Class<? extends MagicProcess>> processById = new HashMap<Short, Class<? extends MagicProcess>>();

    public static Map<String, Class<? extends MagicProcess>> processByName = new HashMap<String, Class<? extends MagicProcess>>();

    public static Map<Class<? extends MagicProcess>, Short> idByProcess = new HashMap<Class<? extends MagicProcess>, Short>();

    public static Map<Class<? extends MagicProcess>, ItemMagicProcess> itemByProcess = new HashMap<Class<? extends MagicProcess>, ItemMagicProcess>();

    static
    {
        List<Class<? extends MagicProcess>> processes = new ArrayList<Class<? extends MagicProcess>>();
        // To remove processes from registry, set them to null
        processes.add(ProcessParticle.class);
        processes.add(ProcessExplosion.class);
        processes.add(ProcessShockwave.class);
        processes.add(ProcessFirebomb.class);
        processes.add(ProcessFireShockwave.class);
        processes.add(ProcessOffset.class);
        processes.add(ProcessMovingOffset.class);
        processes.add(ProcessParallel.class);
        processes.add(ProcessFireball.class);
        processes.add(ProcessAccelerate.class);

        for (short id = 1; id <= processes.size(); id++)
            registerProcess(processes.get(id), id);
    }

    /* ------------------------------------------------------------ */

    public static void registerProcess(Class<? extends MagicProcess> clazz, short id)
    {
        if (clazz == null)
            return;
        if (processById.containsKey(id))
            throw new RuntimeException(String.format("Duplicate assignment of magic process id %d", id));
        processById.put(id, clazz);
        idByProcess.put(clazz, id);
        processByName.put(instantiate(clazz).getName(), clazz);
    }

    public static void registerProcessItem(Class<? extends MagicProcess> clazz, ItemMagicProcess item)
    {
        itemByProcess.put(clazz, item);
    }

    public static MagicProcess createByName(String name)
    {
        return instantiate(processByName.get(name));
    }

    public static MagicProcess createById(short id)
    {
        return instantiate(processById.get(id));
    }

    public static MagicProcess createFromNBT(NBTTagCompound tag)
    {
        Class<? extends MagicProcess> clazz = processById.get(tag.getShort("id"));
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

    private static MagicProcess instantiate(Class<? extends MagicProcess> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (ReflectiveOperationException e)
        {
            // TODO Auto-generated catch block
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
        Short id = idByProcess.get(getClass());
        if (id == null)
            throw new RuntimeException(String.format("Magic process class %s has not been registered", getClass().toString()));
        return id;
    }

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
    /* Magic process information */

    public abstract TargetType[] getValidTargets();

    public abstract int getPsionCost();

    public abstract int getChannelingDuration();

    public abstract int getCastDuration(Target target);

    public abstract String getName();

    public String getUnlocalizedName()
    {
        return "mahouka.process." + getName();
    }

    public String getLocalizedName()
    {
        return StatCollector.translateToLocal(getUnlocalizedName());
    }

    /* ------------------------------------------------------------ */
    /* Item functions */

    public String getItemName()
    {
        return "process_" + getName();
    }

    public Item getItem()
    {
        return itemByProcess.get(getClass());
    }

    public abstract String getTextureName();

    public void registerIcons()
    {
        ItemMagicSequence.registerIcon(getTextureName());
    }

    public void addInformation(List<String> info, boolean isSequence)
    {
        if (isSequence)
            info.add(getLocalizedName());
    }

    /* ------------------------------------------------------------ */

    @SideOnly(Side.CLIENT)
    public void guiUpdate(GuiContainerExt gui)
    {
    }

    @SideOnly(Side.CLIENT)
    public void guiDraw(GuiContainerExt gui)
    {
        String text = StatCollector.translateToLocal("gui.process.no_settings");
        int x = (gui.getWidth() - gui.getFontRenderer().getStringWidth(text)) / 2;
        int y = (gui.getHeight() - 6) / 4;
        gui.getFontRenderer().drawString(text, x, y, 4210752);
    }

    @SideOnly(Side.CLIENT)
    public void guiButtonClick(GuiButton button)
    {
    }

    /* ------------------------------------------------------------ */

    public Target castStart(CastingProcess cp, Target target)
    {
        return target;
    }

    @SideOnly(Side.CLIENT)
    public void castStartClient(CastingProcess cp, Target target)
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

    /* ------------------------------------------------------------ */

    public Target castEnd(CastingProcess cp, Target target)
    {
        return target;
    }

    @SideOnly(Side.CLIENT)
    public void castEndClient(CastingProcess cp, Target target)
    {
        /* do nothing */
    }

    /* ------------------------------------------------------------ */

    public boolean isContinuousCast(Target target)
    {
        return getCastDuration(target) > 0;
    }

}
