package de.yotsuba.mahouka.magic.process;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import de.yotsuba.mahouka.magic.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetPoint;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessExplosion extends MagicProcess
{

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public int getChannelingDuration()
    {
        return Utils.secondsToTicks(3);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return Utils.secondsToTicks(3);
    }

    @Override
    public Target cast(CastingProcess cp, Target target)
    {
        if (target instanceof TargetPoint)
        {
            Vec3 point = ((TargetPoint) target).getPoint();
            World world = cp.getCaster().worldObj;
            return super.cast(cp, target);
        }
        return target;
    }

}
