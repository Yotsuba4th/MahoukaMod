package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessExplosion extends MagicProcess
{

    private boolean blockDamage;
    private boolean fire;

    public ProcessExplosion()
    {
    }

    public ProcessExplosion(boolean fire, boolean blockDamage)
    {
        this.blockDamage = blockDamage;
        this.fire = fire;
    }

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound tag = super.writeToNBT();
        tag.setBoolean("blkdmg", blockDamage);
        tag.setBoolean("fire", fire);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        blockDamage = tag.getBoolean("blkdmg");
        fire = tag.getBoolean("fire");
    }

    @Override
    public int getChannelingDuration()
    {
        return Utils.millisecondsToTicks(3000);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return 0;
    }

    @Override
    public Target cast(CastingProcess cp, Target target)
    {
        float strength = 4f;

        Entity entity = null;
        // if (target instanceof TargetEntity)
        // entity = ((TargetEntity) target).getEntity();

        Vec3 point = target.toTargetPoint().getPoint();
        cp.getCaster().worldObj.newExplosion(entity, point.xCoord, point.yCoord, point.zCoord, strength, fire, blockDamage);

        return target;
    }

}
