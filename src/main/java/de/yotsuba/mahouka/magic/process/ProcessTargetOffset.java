package de.yotsuba.mahouka.magic.process;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.client.gui.GuiContainerExt;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetOffset;

public class ProcessTargetOffset extends MagicProcess
{

    protected Vec3 offset = Vec3.createVectorHelper(0, 0, 0);

    protected Vec3 range = Vec3.createVectorHelper(0, 0, 0);

    /* ------------------------------------------------------------ */

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setDouble("ox", offset.xCoord);
        tag.setDouble("oy", offset.yCoord);
        tag.setDouble("oz", offset.zCoord);
        tag.setDouble("rx", range.xCoord);
        tag.setDouble("ry", range.yCoord);
        tag.setDouble("rz", range.zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        offset.xCoord = tag.getDouble("ox");
        offset.yCoord = tag.getDouble("oy");
        offset.zCoord = tag.getDouble("oz");
        range.xCoord = tag.getDouble("rx");
        range.yCoord = tag.getDouble("ry");
        range.zCoord = tag.getDouble("rz");
    }

    /* ------------------------------------------------------------ */

    @Override
    public String getName()
    {
        return "offset";
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
        return new TargetOffset(target, getRandomOffset());
    }

    public Vec3 getRandomOffset()
    {
        Random rnd = new Random();
        double xd = (rnd.nextDouble() * 2 - 1) * range.xCoord;
        double yd = (rnd.nextDouble() * 2 - 1) * range.yCoord;
        double zd = (rnd.nextDouble() * 2 - 1) * range.zCoord;
        Vec3 rndOffset = Vec3.createVectorHelper(offset.xCoord + xd, offset.yCoord + yd, offset.zCoord + zd);
        return rndOffset;
    }

    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    public void guiUpdate(GuiContainerExt gui)
    {
        gui.getButtons().add(new GuiButtonExt(0, gui.getX() + 8, gui.getY() + 30 + 16 * 0, 16, 16, "+"));
        gui.getButtons().add(new GuiButtonExt(1, gui.getX() + 68, gui.getY() + 30 + 16 * 0, 16, 16, "-"));
        gui.getButtons().add(new GuiButtonExt(2, gui.getX() + 8, gui.getY() + 30 + 16 * 1, 16, 16, "+"));
        gui.getButtons().add(new GuiButtonExt(3, gui.getX() + 68, gui.getY() + 30 + 16 * 1, 16, 16, "-"));
        gui.getButtons().add(new GuiButtonExt(4, gui.getX() + 8, gui.getY() + 30 + 16 * 2, 16, 16, "+"));
        gui.getButtons().add(new GuiButtonExt(5, gui.getX() + 68, gui.getY() + 30 + 16 * 2, 16, 16, "-"));

        gui.getButtons().add(new GuiButtonExt(6, gui.getX() + 92, gui.getY() + 30 + 16 * 0, 16, 16, "+"));
        gui.getButtons().add(new GuiButtonExt(7, gui.getX() + 152, gui.getY() + 30 + 16 * 0, 16, 16, "-"));
        gui.getButtons().add(new GuiButtonExt(8, gui.getX() + 92, gui.getY() + 30 + 16 * 1, 16, 16, "+"));
        gui.getButtons().add(new GuiButtonExt(9, gui.getX() + 152, gui.getY() + 30 + 16 * 1, 16, 16, "-"));
        gui.getButtons().add(new GuiButtonExt(10, gui.getX() + 92, gui.getY() + 30 + 16 * 2, 16, 16, "+"));
        gui.getButtons().add(new GuiButtonExt(11, gui.getX() + 152, gui.getY() + 30 + 16 * 2, 16, 16, "-"));
    }

    @Override
    public void guiButtonClick(int id)
    {
        switch (id)
        {
        case 0:
            offset.xCoord += 1;
            break;
        case 1:
            offset.xCoord -= 1;
            break;
        case 2:
            offset.yCoord += 1;
            break;
        case 3:
            offset.yCoord -= 1;
            break;
        case 4:
            offset.zCoord += 1;
            break;
        case 5:
            offset.zCoord -= 1;
            break;
        case 6:
            range.xCoord += 1;
            break;
        case 7:
            range.xCoord -= 1;
            break;
        case 8:
            range.yCoord += 1;
            break;
        case 9:
            range.yCoord -= 1;
            break;
        case 10:
            range.zCoord += 1;
            break;
        case 11:
            range.zCoord -= 1;
            break;
        default:
            break;
        }
    }

    @Override
    public void guiDraw(GuiContainerExt gui)
    {
        gui.getFontRenderer().drawString("x: " + offset.xCoord, 32, 34 + 16 * 0, GuiContainerExt.getColor(0, 0, 0, 255));
        gui.getFontRenderer().drawString("y: " + offset.yCoord, 32, 34 + 16 * 1, GuiContainerExt.getColor(0, 0, 0, 255));
        gui.getFontRenderer().drawString("z: " + offset.zCoord, 32, 34 + 16 * 2, GuiContainerExt.getColor(0, 0, 0, 255));

        gui.getFontRenderer().drawString("x: " + range.xCoord, 118, 34 + 16 * 0, GuiContainerExt.getColor(0, 0, 0, 255));
        gui.getFontRenderer().drawString("y: " + range.yCoord, 118, 34 + 16 * 1, GuiContainerExt.getColor(0, 0, 0, 255));
        gui.getFontRenderer().drawString("z: " + range.zCoord, 118, 34 + 16 * 2, GuiContainerExt.getColor(0, 0, 0, 255));
    }

}
