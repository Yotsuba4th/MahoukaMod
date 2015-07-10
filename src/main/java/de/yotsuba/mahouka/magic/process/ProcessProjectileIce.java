package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileIce;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public class ProcessProjectileIce extends ProcessProjectile
{

    @Override
    public String getName()
    {
        return "icecrystal";
    }

    @Override
    public int getPsionCost()
    {
        return 20;
    }

    @Override
    public int getChannelingDuration()
    {
        return Utils.millisecondsToTicks(250);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return Utils.millisecondsToTicks(3000);
    }

    @Override
    public Entity createProjectile(CastingProcess cp, Target target)
    {
        return new EntityMagicProjectileIce(target.getWorld(), cp.getCaster(), 0, 0, 0);
    }

    @Override
    public void castStartClient(CastingProcess cp, Target target)
    {
        super.castStartClient(cp, target);
        spawnFx.setColor(0.2f, 0.3f, 1, 1);

    }

}
