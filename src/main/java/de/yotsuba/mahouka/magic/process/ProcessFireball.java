package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.Entity;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.entity.projectile.EntityMagicFireball;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public class ProcessFireball extends ProcessProjectile
{

    @Override
    public String getName()
    {
        return "fireball";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_fireball";
    }

    @Override
    public int getPsionCost()
    {
        return 10;
    }

    @Override
    public int getChannelingDuration()
    {
        return Utils.millisecondsToTicks(250);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return Utils.millisecondsToTicks(1500);
    }

    @Override
    public Entity createProjectile(CastingProcess cp, Target target)
    {
        return new EntityMagicFireball(cp.getCaster().worldObj, cp.getCaster(), 0, 0, 0);
    }
    
}
