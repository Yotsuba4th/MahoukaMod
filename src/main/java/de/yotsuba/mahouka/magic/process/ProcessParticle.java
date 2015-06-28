package de.yotsuba.mahouka.magic.process;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.magic.CastingProcess;
import de.yotsuba.mahouka.magic.Target;
import de.yotsuba.mahouka.magic.Target.TargetPoint;
import de.yotsuba.mahouka.magic.Target.TargetType;
import de.yotsuba.mahouka.util.Utils;

public class ProcessParticle extends MagicProcess
{

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public int getChannelingDuration()
    {
        return Utils.millisecondsToTicks(1000);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return Utils.millisecondsToTicks(3000);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void castClient(CastingProcess cp, Target target)
    {
        /* do nothing */
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void castTickClient(CastingProcess cp, Target target)
    {
        TargetPoint point = target.toPoint();
        //if (new Random().nextInt(3) == 0)
        {
            World world = cp.getCaster().worldObj;
            world.spawnParticle("heart", point.getPoint().xCoord, point.getPoint().yCoord, point.getPoint().zCoord, 0, 0, 0);
        }
    }

}
