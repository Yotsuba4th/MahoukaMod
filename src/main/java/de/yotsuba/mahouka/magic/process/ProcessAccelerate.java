package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;
import de.yotsuba.mahouka.util.target.TargetType;
import de.yotsuba.mahouka.util.target.Targeting;

public class ProcessAccelerate extends MagicProcess
{

    private float speed;

    public ProcessAccelerate()
    {
        speed = 1.5f;
    }

    @Override
    public String getName()
    {
        return "accelerate";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_accelerate";
    }

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.ENTITY };
    }

    @Override
    public int getPsionCost()
    {
        return 5;
    }

    @Override
    public int getChannelingDuration()
    {
        return 1;
    }

    @Override
    public int getCastDuration(Target target)
    {
        return 5;
    }

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        TargetEntity targetEntity = (TargetEntity) target;
        Entity entity = targetEntity.getEntity();

        speed = 1.2f;
        if (entity instanceof Targeting && ((Targeting) entity).getTarget() != null)
        {
            Target entityTarget = ((Targeting) entity).getTarget();
            if (entityTarget instanceof TargetOffset)
                entityTarget = ((TargetOffset) entityTarget).getSource();
            Vec3 delta = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ).subtract(entityTarget.getCurrentPoint()).normalize();
            entity.motionX += delta.xCoord * speed;
            entity.motionY += delta.yCoord * speed;
            entity.motionZ += delta.zCoord * speed;
        }
        else
        {
            // TODO: Use facing direction instead
            Vec3 motion = Vec3.createVectorHelper(entity.motionX, entity.motionY, entity.motionZ).normalize();
            entity.motionX += motion.xCoord * speed;
            entity.motionY += motion.yCoord * speed;
            entity.motionZ += motion.zCoord * speed;
        }

        return target;
    }

}
