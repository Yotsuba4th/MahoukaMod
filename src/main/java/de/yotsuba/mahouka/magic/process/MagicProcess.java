package de.yotsuba.mahouka.magic.process;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.magic.Target;
import de.yotsuba.mahouka.magic.Target.TargetType;
import de.yotsuba.mahouka.magic.cad.CadBase;

public abstract class MagicProcess
{

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("id", MagicProcessManager.getId(getClass()));
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
    }

    public abstract TargetType[] getValidTargets();

    public abstract int getChannelingDuration();

    public abstract int getCastDuration(Target target);

    public void getCasterEffect(/* cast target */)
    {
    }

    public void getTargetChannelingEffect(/* cast target */)
    {
    }

    public void getTargetCastingEffect(/* cast target */)
    {
    }

    public Target cast(CadBase cad, Target target)
    {
        return target;
    }

    /**
     * Called, if isContinuousCast is true
     * 
     * @param cad
     * @param target
     */
    public void castTick(CadBase cad, Target target)
    {
    }

    @SideOnly(Side.CLIENT)
    public void castClient(CadBase cad, Target target)
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
    public void castTickClient(CadBase cad, Target target)
    {
        /* do nothing */
    }

    public boolean isContinuousCast(Target target)
    {
        return getCastDuration(target) > 0;
    }

}
