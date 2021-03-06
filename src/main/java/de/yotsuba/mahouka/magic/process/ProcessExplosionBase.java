package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public abstract class ProcessExplosionBase extends MagicProcess
{

    private final boolean blockDamage;

    private final boolean fire;

    /* ------------------------------------------------------------ */

    public ProcessExplosionBase(boolean fire, boolean blockDamage)
    {
        this.blockDamage = blockDamage;
        this.fire = fire;
    }

    /* ------------------------------------------------------------ */

    @Override
    public int getChannelingDuration()
    {
        return Utils.millisecondsToTicks(3000);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return 20;
    }

    /* ------------------------------------------------------------ */

    @Override
    public Target castEnd(CastingProcess cp, Target target)
    {
        float strength = 4f;

        Entity entity = null;
        // if (target instanceof TargetEntity)
        // entity = ((TargetEntity) target).getEntity();

        Vec3 point = target.getPoint();
        target.getWorld().newExplosion(entity, point.xCoord, point.yCoord, point.zCoord, strength, fire, blockDamage);

        return target;
    }

}
