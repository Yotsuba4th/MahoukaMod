package de.yotsuba.mahouka.magic.process;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.gui.GuiContainerExt;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetOffset;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessOffset extends MagicProcess
{

    private Vec3 offset;

    private Vec3 range;

    public ProcessOffset()
    {
        offset = Vec3.createVectorHelper(0, 5, 0);
        range = Vec3.createVectorHelper(0, 0, 0);
    }

    @Override
    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = super.writeToNBT();
        tag.setDouble("ox", offset.xCoord);
        tag.setDouble("oy", offset.yCoord);
        tag.setDouble("oz", offset.zCoord);
        tag.setDouble("rx", range.xCoord);
        tag.setDouble("ry", range.yCoord);
        tag.setDouble("rz", range.zCoord);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        offset.xCoord = tag.getDouble("ox");
        offset.yCoord = tag.getDouble("oy");
        offset.zCoord = tag.getDouble("oz");
        range.xCoord = tag.getDouble("rx");
        range.yCoord = tag.getDouble("ry");
        range.zCoord = tag.getDouble("rz");
    }

    @Override
    public String getName()
    {
        return "offset";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_offset";
    }

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
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
        return new TargetOffset(target, offset);
    }

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
    @SideOnly(Side.CLIENT)
    public void guiButtonClick(GuiButton button)
    {
        switch (button.id)
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
