package de.yotsuba.mahouka.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import de.yotsuba.mahouka.magic.process.MagicProcess;

public class ActivationSequence
{

    protected List<MagicProcess> processes = new ArrayList<MagicProcess>();

    public void getCasterChannelingEffects(/* cast target */)
    {
    }

    public void getTargetChannelingEffects(/* cast target */)
    {
    }

    public void writeToNBT(NBTTagCompound tag)
    {
    }

    public void readFromNBT(NBTTagCompound tag)
    {
    }

}
