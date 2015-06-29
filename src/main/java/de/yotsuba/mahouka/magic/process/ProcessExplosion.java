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

    private boolean smoke;
    private boolean fire;

    public ProcessExplosion()
    {
    }

    public ProcessExplosion(boolean withFire, boolean withSmoke)
    {
        smoke = withSmoke;
        fire = withFire;
    }

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
            float size = 4f;
            Vec3 point = ((TargetPoint) target).getPoint();
            double x = point.xCoord;
            double y = point.yCoord;
            double z = point.zCoord;
            World world = cp.getCaster().worldObj;
            if (!world.isRemote)
                world.newExplosion(null, x, y, z, size, fire, smoke);
            return super.cast(cp, target);
        }
        return target;
    }
}
