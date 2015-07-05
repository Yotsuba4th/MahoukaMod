package de.yotsuba.mahouka.client.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetEntity;

public class EffectTarget extends Effect
{

    protected Target target;

    public boolean isAiming;

    public boolean snapToGround;

    public EffectTarget(Target target, double x, double y, double z, boolean isAiming)
    {
        super(x, y, z);
        this.target = target;
        this.isAiming = isAiming;
        this.snapToGround = true;
        updateTarget();
    }

    public EffectTarget(Target target, boolean aimMode)
    {
        this(target, 0, 0, 0, aimMode);
    }

    @Override
    public void update()
    {
        super.update();
        updateTarget();
    }

    protected void updateTarget()
    {
        if (target == null)
            return;
        Vec3 targetPoint = target.getCurrentPoint();
        if (isAiming)
        {
            lookAt(targetPoint);
        }
        else
        {
            EntityPlayer player = target instanceof TargetEntity ? ((TargetEntity) target).getPlayer() : null;
            if (player != null && player.capabilities.isFlying)
                setPosition(targetPoint.xCoord, targetPoint.yCoord - player.height * 0.49, targetPoint.zCoord);
            else if (snapToGround)
                setPositionOnGround(Minecraft.getMinecraft().theWorld, targetPoint.xCoord, targetPoint.yCoord, targetPoint.zCoord);
            else
                setPosition(targetPoint.xCoord, targetPoint.yCoord + 0.01, targetPoint.zCoord);
        }
    }

}
