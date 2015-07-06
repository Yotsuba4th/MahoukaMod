package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.client.effect.Effect;
import de.yotsuba.mahouka.client.effect.EffectCast;
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

    @SideOnly(Side.CLIENT)
    protected Effect targetFx;

    @SideOnly(Side.CLIENT)
    protected Effect spawnFx;

    protected Vec3 spawnPoint;

    protected Target sourceTarget;

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.POINT };
    }

    /* ------------------------------------------------------------ */

    public abstract Entity createProjectile(CastingProcess cp, Target target);

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        spawnPoint = target.getCurrentPoint();
        sourceTarget = (target instanceof TargetOffset) ? ((TargetOffset) target).getSource() : null;
        return target;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void castStartClient(CastingProcess cp, Target target)
    {
        spawnPoint = target.getCurrentPoint();
        sourceTarget = (target instanceof TargetOffset) ? ((TargetOffset) target).getSource() : null;
        int castDuration = getCastDuration(target);

        spawnFx = new EffectCast(sourceTarget, spawnPoint.xCoord, spawnPoint.yCoord, spawnPoint.zCoord, true);
        spawnFx.setIcon(MahoukaMod.icon_rune_default);
        spawnFx.setScale(1);
        spawnFx.setColor(1, 0, 0, 1);
        spawnFx.vRoll = 6;
        spawnFx.fadeIn = 10;
        spawnFx.fadeOut = 5;
        spawnFx.setMaxAge(castDuration + spawnFx.fadeOut);
        EffectRenderer.addEffect(spawnFx, cp.getId());

        if (sourceTarget != null)
        {
            targetFx = new EffectCast(sourceTarget, false);
            targetFx.setIcon(MahoukaMod.icon_rune_default);
            targetFx.setScale(1);
            targetFx.setColor(0, 0, 0, 0.25f);
            targetFx.vRoll = -2;
            targetFx.fadeIn = 10;
            targetFx.fadeOut = 10;
            targetFx.setMaxAge(castDuration + targetFx.fadeOut + 10);
            if (!EffectRenderer.hasSimilarEffect(targetFx))
                EffectRenderer.addEffect(targetFx, cp.getId());
        }
    }

    /* ------------------------------------------------------------ */

    @Override
    public Target castEnd(CastingProcess cp, Target target)
    {
        Vec3 targetPoint = sourceTarget == null ? null : sourceTarget.getPoint();

        Entity entity = createProjectile(cp, target);
        entity.setLocationAndAngles(spawnPoint.xCoord, spawnPoint.yCoord, spawnPoint.zCoord, cp.getCaster().rotationYaw, cp.getCaster().rotationPitch);
        if (targetPoint != null)
            Utils.setEntityHeading(entity, targetPoint);

        if (entity instanceof Targeting)
            ((Targeting) entity).setTarget(target);

        cp.getCaster().worldObj.spawnEntityInWorld(entity);
        return new TargetEntity(entity, false, true);
    }

}
