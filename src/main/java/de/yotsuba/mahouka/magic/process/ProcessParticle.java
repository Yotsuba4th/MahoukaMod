package de.yotsuba.mahouka.magic.process;

import java.util.Random;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.MahoukaMod;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;

public class ProcessParticle extends MagicProcess
{

    @Override
    public String getName()
    {
        return "particle";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_particles";
    }

    @Override
    public int getPsionCost()
    {
        return 5;
    }
    
    @Override
    public int getChannelingDuration()
    {
        return Utils.secondsToTicks(1);
    }

    @Override
    public int getCastDuration(Target target)
    {
        return Utils.secondsToTicks(5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void castTickClient(CastingProcess cp, Target target)
    {
        // if (new Random().nextInt(3) == 0)
        {
            World world = cp.getCaster().worldObj;
            Vec3 point = target.getPoint();
            double x = point.xCoord + new Random().nextGaussian() * 0.5;
            double z = point.zCoord + new Random().nextGaussian() * 0.5;
            world.spawnParticle("witchMagic", x, point.yCoord + 1, z, 0, 0, 0);
        }
    }

}
