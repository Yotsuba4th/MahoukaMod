package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetOffset;
import de.yotsuba.mahouka.util.target.TargetType;
import de.yotsuba.mahouka.util.target.Targeting;

public class ProcessAccelerate extends MagicProcess
{

    private float speed = 1.0f;

    /* ------------------------------------------------------------ */

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setFloat("speed", speed);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        speed = tag.getFloat("speed");
        // TODO DEBUG
        speed = 1;
    }

    /* ------------------------------------------------------------ */

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

    /* ------------------------------------------------------------ */

    public TargetEntity getEntityTarget(Target target)
    {
        Target tmpTarget = target;
        while (target instanceof TargetOffset)
        {
            TargetOffset targetOffset = (TargetOffset) target;
            tmpTarget = targetOffset.getSource();
        }
        if (!(target instanceof TargetEntity))
            return null;
        return (TargetEntity) tmpTarget;
    }

    @Override
    public Target castStart(CastingProcess cp, Target target)
    {
        TargetEntity targetEntity = getEntityTarget(target);
        if (targetEntity == null)
            return null;
        Entity entity = targetEntity.getEntity();

        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            entityLiving.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Utils.secondsToTicks(10), 2));
        }
        else if (entity instanceof Targeting && ((Targeting) entity).getTarget() != null)
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
            if (targetEntity != target)
            {
                Vec3 dir = target.getPoint().subtract(targetEntity.getPoint()).normalize();
                entity.motionX += dir.xCoord * speed;
                entity.motionY += dir.yCoord * speed;
                entity.motionZ += dir.zCoord * speed;
            }
            else
            {
                // TODO: Use facing direction instead
                Vec3 dir = Vec3.createVectorHelper(entity.motionX, entity.motionY, entity.motionZ).normalize();
                entity.motionX += dir.xCoord * speed;
                entity.motionY += dir.yCoord * speed;
                entity.motionZ += dir.zCoord * speed;
            }
        }

        return target;
    }
}
