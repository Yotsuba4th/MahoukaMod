package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileFire;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public class ProcessProjectileFire extends ProcessProjectile
{

    @Override
    public String getName()
    {
        return "fireball";
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
        return new EntityMagicProjectileFire(target.getWorld(), cp.getCaster(), 0, 0, 0);
    }
    
}
