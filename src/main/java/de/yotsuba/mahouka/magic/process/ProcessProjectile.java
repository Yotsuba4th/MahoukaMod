package de.yotsuba.mahouka.magic.process;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.entity.fx.EntityFxRune;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;
import de.yotsuba.mahouka.util.target.TargetType;
import de.yotsuba.mahouka.util.target.Targeting;

public abstract class ProcessProjectile extends MagicProcess
{

    private EntityFxRune targetFx;

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

        EntityFxRune spawnFx = new EntityFxRune(cp.getCaster().worldObj, point.xCoord, point.yCoord + 0.01, point.zCoord, 0, 0, 0);
        spawnFx.setFlat(false);
        spawnFx.setRadius(1);
        spawnFx.setColor(1, 0, 0);
        Minecraft.getMinecraft().effectRenderer.addEffect(spawnFx);

        point = getTargetPoint(target);
        targetFx = new EntityFxRune(cp.getCaster().worldObj, point.xCoord, point.yCoord + 0.01, point.zCoord, 0, 0, 0);
        targetFx.setRadius(1);
        targetFx.setFlat(true);
        Minecraft.getMinecraft().effectRenderer.addEffect(targetFx);
    }

    @Override
    public void castTickClient(CastingProcess cp, Target target)
    {
        Vec3 point = getTargetPoint(target);
        targetFx.setPositionDropped(point.xCoord, point.yCoord, point.zCoord);
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
