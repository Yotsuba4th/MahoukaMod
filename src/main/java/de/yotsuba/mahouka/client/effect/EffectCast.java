package de.yotsuba.mahouka.client.effect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.entity.fx.EntityCastBreakingFX;
import de.yotsuba.mahouka.util.target.Target;

public class EffectCast extends EffectTarget
{

    public EffectCast(Target target, double x, double y, double z, boolean isAiming)
    {
        super(target, x, y, z, isAiming);
        setIcon(MahoukaMod.icon_rune_default);
        setColor(0.5f, 0.7f, 1, 0.5f);
        fadeIn = 4;
        fadeOut = 4;
        pitch = -90;
        scale = 1.5f;
        vRoll = 6;
    }

    public EffectCast(Target target, boolean isAiming)
    {
        this(target, 0, 0, 0, isAiming);
    }

    @Override
    public void cancel()
    {
        super.cancel();
        Random rnd = new Random();
        WorldClient world = Minecraft.getMinecraft().theWorld;
        for (int i = 0; i < 10; i++)
        {
            // TODO (4) Offset particles only in the effect plane!
            double px = x + rnd.nextGaussian() * scale * 0.25;
            double py = y + rnd.nextGaussian() * scale * 0.25;
            double pz = z + rnd.nextGaussian() * scale * 0.25;

            EntityCastBreakingFX fx = new EntityCastBreakingFX(world, px, py, pz);
            fx.setParticleIcon(icon);
            fx.setColor(r, g, b, a);
            Minecraft.getMinecraft().effectRenderer.addEffect(fx);
            // world.spawnParticle("heart", px, py, pz, vx, vy, vz);
        }
        age = maxAge - fadeOut;
    }

}
