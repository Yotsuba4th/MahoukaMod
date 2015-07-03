package de.yotsuba.mahouka.client.effect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import de.yotsuba.mahouka.entity.fx.EntityCastBreakingFX;

public class EffectCast extends Effect
{

    public EffectCast(double x, double y, double z)
    {
        super(x, y, z);
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
