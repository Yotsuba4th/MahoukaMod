package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetType;
import de.yotsuba.mahouka.util.target.Targeting;

public abstract class ProcessProjectile extends MagicProcess
{

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        return target;
    }

    @Override
    public void castStartClient(CastingProcess cp, Target target)
    {
    }

    @Override
    public Target castEnd(CastingProcess cp, Target target)
    {
        Entity entity = createProjectile(cp, target);
        Vec3 point = target.getPoint();
        entity.setLocationAndAngles(point.xCoord, point.yCoord, point.zCoord, 0, 0); // TODO: Calculate heading
        if (entity instanceof Targeting)
            ((Targeting) entity).setTarget(target);
        cp.getCaster().worldObj.spawnEntityInWorld(entity);
        return new TargetEntity(entity, false, true);
    }

    public abstract Entity createProjectile(CastingProcess cp, Target target);

}
