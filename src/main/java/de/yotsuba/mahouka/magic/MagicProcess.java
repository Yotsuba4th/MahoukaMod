package de.yotsuba.mahouka.magic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.gui.GuiContainerExt;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public abstract class MagicProcess
{

    public static Map<Class<? extends MagicProcess>, ItemMagicProcess> itemByProcess = new HashMap<Class<? extends MagicProcess>, ItemMagicProcess>();

    /* ------------------------------------------------------------ */

    public static void registerProcessItem(Class<? extends MagicProcess> clazz, ItemMagicProcess item)
    {
        itemByProcess.put(clazz, item);
    }

    public static MagicProcess createFromStack(ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof ItemMagicProcess))
            return null;
        ItemMagicProcess item = (ItemMagicProcess) stack.getItem();
        MagicProcess process = item.getItemProcess().copy();
        process.readFromNBT(stack.getTagCompound());
        return process;
    }

    /* ------------------------------------------------------------ */

    protected String displayName;

    /* ------------------------------------------------------------ */

    public void writeToNBT(NBTTagCompound tag)
    {
        if (displayName != null)
        {
            NBTTagCompound display = tag.getCompoundTag("display");
            display.setString("Name", displayName);
            tag.setTag("display", display);
        }
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        displayName = null;
        if (tag.hasKey("display"))
        {
            NBTTagCompound display = tag.getCompoundTag("display");
            if (display.hasKey("Name"))
                displayName = display.getString("Name");
        }
    }

    public MagicProcess copy()
    {
        try
        {
            MagicProcess result = getClass().newInstance();
            result.displayName = displayName;
            return result;
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException("Unexpected error");
        }
    }

    /* ------------------------------------------------------------ */
    /* Magic process information */

    // TODO (2) This does not cover cases where e.g. ENTITY is allowed, but not SELF
    public abstract TargetType[] getValidTargets();

    public boolean isTargetValid(Target target)
    {
        for (TargetType type : getValidTargets())
            if (target.matchesType(type))
                return true;
        return false;
    }

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

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /* ------------------------------------------------------------ */
    /* Item functions */

    public String getItemName()
    {
        return "process_" + getName();
    }

    public ItemMagicProcess createItem()
    {
        return new ItemMagicProcess(this);
    }

    public Item getItem()
    {
        return itemByProcess.get(getClass());
    }

    public ItemStack getItemStack()
    {
        ItemStack stack = new ItemStack(getItem());
        NBTTagCompound tag = new NBTTagCompound();
        stack.setTagCompound(tag);
        if (displayName != null)
            stack.setStackDisplayName(displayName);
        writeToNBT(tag);
        return stack;
    }

    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_" + getName();
    }

    public void addInformation(List<String> info, boolean isRoot)
    {
        if (isRoot)
        {
            info.add("Psion cost  : " + getPsionCost());
            info.add("Channel time: " + getChannelingDuration());
        }
        else
        {
            info.add((displayName != null) ? displayName : getLocalizedName());
        }
    }

    /* ------------------------------------------------------------ */

    @SideOnly(Side.CLIENT)
    public void guiUpdate(GuiContainerExt gui)
    {
        /* do nothing */
    }

    @SideOnly(Side.CLIENT)
    public void guiDraw(GuiContainerExt gui)
    {
        String text = StatCollector.translateToLocal("gui.process.no_settings");
        int x = (gui.getWidth() - gui.getFontRenderer().getStringWidth(text)) / 2;
        int y = (gui.getHeight() - 6) / 4;
        gui.getFontRenderer().drawString(text, x, y, 4210752);
    }

    public void guiButtonClick(int id)
    {
        /* do nothing */
    }

    /* ------------------------------------------------------------ */

    public boolean castCancel(CastingProcess cp, Target target)
    {
        return true;
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
        /* do nothing */
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
