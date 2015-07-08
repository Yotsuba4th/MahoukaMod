package de.yotsuba.mahouka.magic.process;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.client.gui.GuiContainerExt;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetAhead;
import de.yotsuba.mahouka.util.target.TargetEntity;

public class ProcessTargetAhead extends MagicProcess
{

    protected float distance = 2;

    /* ------------------------------------------------------------ */

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setFloat("d", distance);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        distance = tag.getFloat("d");
    }

    /* ------------------------------------------------------------ */

    @Override
    public String getName()
    {
        return "offset_ahead";
    }

    @Override
    public boolean isTargetValid(Target target)
    {
        return target instanceof TargetEntity;
    }

    @Override
    public int getPsionCost()
    {
        return 0;
    }

    @Override
    public int getChannelingDuration()
    {
        return 0;
    }

    @Override
    public int getCastDuration(Target target)
    {
        return 0;
    }

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        if (target instanceof TargetEntity)
            return new TargetAhead((TargetEntity) target, distance);
        else
            return null;
    }

    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    public void guiUpdate(GuiContainerExt gui)
    {
        gui.getButtons().add(new GuiButtonExt(0, gui.getX() + 8, gui.getY() + 30 + 16 * 0, 16, 16, "-"));
        gui.getButtons().add(new GuiButtonExt(1, gui.getX() + 98, gui.getY() + 30 + 16 * 0, 16, 16, "+"));
    }

    @Override
    public void guiButtonClick(int id)
    {
        switch (id)
        {
        case 0:
            distance -= 0.25f;
            break;
        case 1:
            distance += 0.25f;
            break;
        default:
            break;
        }
    }

    @Override
    public void guiDraw(GuiContainerExt gui)
    {
        gui.getFontRenderer().drawString("offset: " + distance, 32, 34 + 16 * 0, GuiContainerExt.getColor(0, 0, 0, 255));
    }

}
