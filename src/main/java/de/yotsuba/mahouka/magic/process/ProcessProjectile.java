package de.yotsuba.mahouka.magic.process;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.entity.fx.EntityFxDirected;
import de.yotsuba.mahouka.entity.fx.EntityFxExt;
import de.yotsuba.mahouka.entity.fx.EntityFxFlat;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;
import de.yotsuba.mahouka.util.target.TargetType;
import de.yotsuba.mahouka.util.target.Targeting;

public abstract class ProcessProjectile extends MagicProcess
{

    protected EntityFxExt targetFx;

    protected EntityFxDirected spawnFx;

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    /* ------------------------------------------------------------ */

    private Vec3 getTargetPoint(Target target)
    {
        if (target instanceof TargetOffset)
        {
            TargetOffset targetOffset = (TargetOffset) target;
            Vec3 point = targetOffset.getSource().getCurrentPoint();
            return point;
        }
        return null;
    }

    public abstract Entity createProjectile(CastingProcess cp, Target target);

    /* ------------------------------------------------------------ */

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        return target;
    }

    @Override
    public void castStartClient(CastingProcess cp, Target target)
    {
        Vec3 point = target.getCurrentPoint();
        Vec3 targetPoint = getTargetPoint(target);

        createSpawnEffect(cp, point);
        if (spawnFx != null)
        {
            spawnFx.setMaxAge(getCastDuration(target));
            if (targetPoint != null)
                spawnFx.lookAt(targetPoint);
            Minecraft.getMinecraft().effectRenderer.addEffect(spawnFx);
        }

        if (targetPoint != null)
        {
            createTargetEffect(cp, targetPoint);
            if (targetFx != null)
            {
                targetFx.setMaxAge(getCastDuration(target));
                Minecraft.getMinecraft().effectRenderer.addEffect(targetFx);
            }
        }
    }

    private void createTargetEffect(CastingProcess cp, Vec3 point)
    {
        // TODO: Allow detection of same effects at the same location and prevent it
        targetFx = new EntityFxFlat(cp.getCaster().worldObj, point.xCoord, point.yCoord, point.zCoord, 0, 0, 0);
        targetFx.setParticleIcon(MahoukaMod.icon_rune_default);
        targetFx.setRadius(1);
    }

    public void createSpawnEffect(CastingProcess cp, Vec3 point)
    {
        spawnFx = new EntityFxDirected(cp.getCaster().worldObj, point.xCoord, point.yCoord, point.zCoord, 0, 0, 0);
        spawnFx.setParticleIcon(MahoukaMod.icon_rune_default);
        spawnFx.setRadius(1);
        spawnFx.setColor(1, 0, 0);
    }

    /* ------------------------------------------------------------ */

    @Override
    public void castTickClient(CastingProcess cp, Target target)
    {
        Vec3 targetPoint = getTargetPoint(target);
        if (targetPoint != null)
        {
            targetFx.setPositionOnGround(targetPoint.xCoord, targetPoint.yCoord, targetPoint.zCoord);
            spawnFx.lookAt(targetPoint);
        }
    }

    /* ------------------------------------------------------------ */

    @Override
    public Target castEnd(CastingProcess cp, Target target)
    {
        Vec3 point = target.getPoint();
        Vec3 targetPoint = getTargetPoint(target);

        Entity entity = createProjectile(cp, target);
        entity.setLocationAndAngles(point.xCoord, point.yCoord, point.zCoord, cp.getCaster().rotationYaw, cp.getCaster().rotationPitch);
        if (targetPoint != null)
            Utils.setEntityHeading(entity, targetPoint);

        if (entity instanceof Targeting)
            ((Targeting) entity).setTarget(target);

        cp.getCaster().worldObj.spawnEntityInWorld(entity);
        return new TargetEntity(entity, false, true);
    }

}
