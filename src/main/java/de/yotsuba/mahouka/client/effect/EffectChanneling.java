package de.yotsuba.mahouka.client.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.util.target.Target;

public class EffectChanneling extends Effect
{

    protected Target target;

    public EffectChanneling(Target target)
    {
        super(0, -0.5, 0);
        this.target = target;
        setIcon(MahoukaMod.icon_rune_default);
        setColor(0.5f, 0.7f, 1, 0.5f);
        fadeIn = 4;
        fadeOut = 4;
        pitch = -90;
        scale = 1.5f;
    }

    @Override
    public void update()
    {
        super.update();
        Vec3 targetPoint = target.getCurrentPoint();
        setPositionOnGround(Minecraft.getMinecraft().theWorld, targetPoint.xCoord, targetPoint.yCoord, targetPoint.zCoord);
        roll += 6.0f;
    }

}
