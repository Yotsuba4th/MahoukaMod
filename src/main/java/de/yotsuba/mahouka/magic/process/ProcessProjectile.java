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
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;
import de.yotsuba.mahouka.util.target.TargetType;
import de.yotsuba.mahouka.util.target.Targeting;

public abstract class ProcessProjectile extends MagicProcess
{

    private EntityFxExt targetFx;

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
        Vec3 point = target.getCurrentPoint();
        Vec3 targetPoint = getTargetPoint(target);

        EntityFxExt spawnFx = new EntityFxDirected(cp.getCaster().worldObj, point.xCoord, point.yCoord + 0.01, point.zCoord, 0, 0, 0);
        spawnFx.setParticleIcon(MahoukaMod.icon_rune_default);
        spawnFx.setRadius(1);
        spawnFx.setColor(1, 0, 0);
        spawnFx.setMaxAge(getCastDuration(target));
        // spawnFx.rotationPitch = cp.getCaster().rotationPitch;
        // spawnFx.rotationYaw = cp.getCaster().rotationYaw;
        Minecraft.getMinecraft().effectRenderer.addEffect(spawnFx);
        {
            double xd = targetPoint.xCoord - point.xCoord;
            double yd = targetPoint.yCoord - point.zCoord;
            double zd = targetPoint.zCoord - point.zCoord;
            spawnFx.rotationYaw = (float) (Math.atan2(zd, xd) * 180.0D / Math.PI) - 90.0F;
            double f = Math.sqrt(xd * xd + zd * zd) / yd;
            spawnFx.rotationPitch = - (float) ((Math.atan(f) * 180.0D / Math.PI) - 90);

        }

        targetPoint = getTargetPoint(target);
        targetFx = new EntityFxFlat(cp.getCaster().worldObj, targetPoint.xCoord, targetPoint.yCoord + 0.01, targetPoint.zCoord, 0, 0, 0);
        targetFx.setParticleIcon(MahoukaMod.icon_rune_default);
        targetFx.setRadius(1);
        targetFx.setMaxAge(getCastDuration(target));
        Minecraft.getMinecraft().effectRenderer.addEffect(targetFx);
    }

    @Override
    public void castTickClient(CastingProcess cp, Target target)
    {
        Vec3 point = getTargetPoint(target);
        targetFx.setPositionOnGround(point.xCoord, point.yCoord, point.zCoord);
        // targetFx.setPosition(point.xCoord, targetFx.dropOnBlock(point.xCoord, point.yCoord, point.zCoord) + 0.501, point.zCoord);
        // double x = point.xCoord + new Random().nextGaussian() * 0.15;
        // double z = point.zCoord + new Random().nextGaussian() * 0.15;
        // World world = cp.getCaster().worldObj;
        // world.spawnParticle("instantSpell", x, point.yCoord, z, 0, 0, 0);
    }

    private Vec3 getTargetPoint(Target target)
    {
        TargetOffset targetOffset = (TargetOffset) target;
        Vec3 point = targetOffset.getSource().getCurrentPoint();
        return point;
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
