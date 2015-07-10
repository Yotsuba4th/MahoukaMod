package de.yotsuba.mahouka.magic.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.client.gui.GuiContainerExt;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;

public class ProcessParallel extends ProcessSequence
{

    protected byte ticksPerProcess = 10;

    /* ------------------------------------------------------------ */

    protected List<CastingProcess> casts = new ArrayList<CastingProcess>();

    protected int time;

    protected int index;

    /* ------------------------------------------------------------ */

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setByte("tpp", ticksPerProcess);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        setTicksPerProcess(tag.getByte("tpp"));
    }

    /* ------------------------------------------------------------ */

    @Override
    public String getName()
    {
        return "parallel";
    }

    @Override
    public boolean isTargetValid(Target target)
    {
        for (MagicProcess process : processes)
            if (!process.isTargetValid(target))
                return false;
        return true;
    }

    @Override
    public void addInformation(List<String> info, boolean isRoot)
    {
        if (isRoot)
        {
            info.add("Psion cost  : " + getPsionCost());
            info.add("Channel time: " + getChannelingDuration());
        }
        for (MagicProcess seq : processes)
        {
            int oldSize = info.size();
            seq.addInformation(info, false);
            for (int i = oldSize + 1; i < info.size(); i++)
                info.set(i, "   " + info.get(i));
        }
    }

    @Override
    public int getPsionCost()
    {
        return super.getPsionCost();
    }

    @Override
    public int getChannelingDuration()
    {
        return super.getChannelingDuration() / 2;
    }

    @Override
    public int getCastDuration(Target target)
    {
        if (index < processes.size())
            return Integer.MAX_VALUE;
        cleanCasts();
        return casts.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    public byte getTicksPerProcess()
    {
        return ticksPerProcess;
    }

    public void setTicksPerProcess(int ticksPerProcess)
    {
        this.ticksPerProcess = (byte) Math.max(2, Math.min(20, ticksPerProcess));
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
            setTicksPerProcess(ticksPerProcess - 1);
            break;
        case 1:
            setTicksPerProcess(ticksPerProcess + 1);
            break;
        default:
            break;
        }
    }

    @Override
    public void guiDraw(GuiContainerExt gui)
    {
        gui.getFontRenderer().drawString("tpp: " + ticksPerProcess, 32, 34 + 16 * 0, GuiContainerExt.getColor(0, 0, 0, 255));
    }

    /* ------------------------------------------------------------ */

    private void cleanCasts()
    {
        for (Iterator<CastingProcess> it = casts.iterator(); it.hasNext();)
        {
            CastingProcess cast = it.next();
            if (!cast.isActive())
                it.remove();
        }
    }

    @Override
    public boolean castCancel(CastingProcess cp, Target target)
    {
        cleanCasts();
        if (casts.isEmpty())
            return true;
        boolean cancelledAny = false;
        for (CastingProcess castingProcess : casts)
            cancelledAny |= castingProcess.cancel(false);
        cleanCasts();
        return cancelledAny;
    }

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        casts.clear();
        time = 0;
        index = 0;
        return target;
    }

    @Override
    public void castTick(CastingProcess cp, Target target)
    {
        if (index < processes.size())
        {
            time++;
            if (time >= ticksPerProcess)
            {
                MagicProcess sequence = processes.get(index);
                UUID id = UUID.randomUUID();
                CastingProcess cast = new CastingProcess(cp.getCaster(), sequence, target, id, 0, 0);
                cast.start();
                casts.add(cast);

                index++;
                time = 0;
            }
        }
    }

}
