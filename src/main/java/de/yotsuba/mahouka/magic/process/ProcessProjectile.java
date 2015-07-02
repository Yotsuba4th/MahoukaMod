package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.effect.Effect;
import de.yotsuba.mahouka.client.effect.EffectRenderer;
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

    protected Effect targetFx;

    protected Effect spawnFx;

    protected int castDurationCache;

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
    public void castStartClient(CastingProcess cp, Target target)
    {
        castDurationCache = getCastDuration(target);
        Vec3 point = target.getCurrentPoint();
        Vec3 targetPoint = getTargetPoint(target);

        createSpawnEffect(cp, point);
        if (spawnFx != null)
        {
            if (targetPoint != null)
                spawnFx.lookAt(targetPoint);
            EffectRenderer.addEffect(spawnFx, cp.getId());
        }

        if (targetPoint != null)
        {
            createTargetEffect(cp, targetPoint);
            if (targetFx != null)
            {
                targetFx.pitch = -90;
                EffectRenderer.addEffect(targetFx, cp.getId());
            }
        }
    }

    private void createTargetEffect(CastingProcess cp, Vec3 point)
    {
        // TODO: Allow detection of same effects at the same location and prevent it
        targetFx = new Effect(point.xCoord, point.yCoord + 0.01, point.zCoord);
        targetFx.setIcon(MahoukaMod.icon_rune_default);
        targetFx.setScale(1);
        targetFx.setColor(0, 0, 0, 0.25f);
        targetFx.vRoll = -2;
        targetFx.fadeIn = 10;
        targetFx.fadeOut = 10;
        targetFx.setMaxAge(castDurationCache + targetFx.fadeOut + 10);
    }

    public void createSpawnEffect(CastingProcess cp, Vec3 point)
    {
        spawnFx = new Effect(point.xCoord, point.yCoord + 0.01, point.zCoord);
        spawnFx.setIcon(MahoukaMod.icon_rune_default);
        spawnFx.setScale(1);
        spawnFx.setColor(1, 0, 0, 1);
        spawnFx.vRoll = 6;
        spawnFx.fadeIn = 10;
        spawnFx.fadeOut = 5;
        spawnFx.pitch = -90;
        spawnFx.setMaxAge(castDurationCache + spawnFx.fadeOut);
    }

    /* ------------------------------------------------------------ */

    @Override
    public void castTickClient(CastingProcess cp, Target target)
    {
        Vec3 targetPoint = getTargetPoint(target);
        if (targetPoint != null)
        {
            if (targetFx != null)
                targetFx.setPositionOnGround(cp.getWorld(), targetPoint.xCoord, targetPoint.yCoord + 0.1, targetPoint.zCoord);
            if (spawnFx != null)
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
