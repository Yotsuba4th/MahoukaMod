package de.yotsuba.mahouka.magic.process;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetPoint;
import de.yotsuba.mahouka.util.target.TargetType;

public class ProcessDecomposition extends MagicProcess
{

    public static final String NAME = "decomposition";

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public TargetType[] getValidTargets()
    {
        return new TargetType[] { TargetType.ENTITY, TargetType.BLOCK };
    }

    @Override
    public int getPsionCost()
    {
        return 1000;
    }

    @Override
    public int getChannelingDuration()
    {
        return Utils.secondsToTicks(1);
    }

    @Override
    public int getCastDuration(Target target)
    {
        if (target instanceof TargetBlock)
            return Utils.millisecondsToTicks(500);
        else
            return Utils.secondsToTicks(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void castTickClient(CastingProcess cp, Target target)
    {
        Random rand = new Random();
        World world = cp.getWorld();
        Vec3 point = target.getCurrentPoint();
        String fx = "cloud";

        if (target instanceof TargetEntity)
        {
            Entity entity = ((TargetEntity) target).getEntity();
            double volume = entity.width * entity.width * entity.height * 2;
            for (int i = 0; i < volume; i++)
            {
                double scale = 0.5;
                double x = point.xCoord + rand.nextGaussian() * entity.width * scale;
                double y = point.yCoord + rand.nextDouble() * entity.height;
                double z = point.zCoord + rand.nextGaussian() * entity.width * scale;
                cp.getWorld().spawnParticle(fx, x, y, z, 0, 0, 0);
            }
        }
        else
        {
            double scale = 0.5;
            double x = point.xCoord + rand.nextGaussian() * scale;
            double y = point.yCoord + rand.nextGaussian() * scale;
            double z = point.zCoord + rand.nextGaussian() * scale;
            cp.getWorld().spawnParticle(fx, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public Target castEnd(CastingProcess cp, Target target)
    {
        DamageSource d = new EntityDamageSource(NAME, cp.getCaster()).setMagicDamage().setDamageBypassesArmor().setDamageIsAbsolute();

        if (target instanceof TargetEntity)
        {
            Entity entity = ((TargetEntity) target).getEntity();
            if (!entity.isEntityInvulnerable())
            {
                entity.setDead();
            }
        }
        else if (target instanceof TargetBlock)
        {
            TargetBlock block = (TargetBlock) target;
            if (block.getBlock().getBlockHardness(null, 0, 0, 0) >= 0)
            {
                cp.getWorld().setBlock(block.getX(), block.getY(), block.getZ(), Blocks.air);
            }
        }

        return new TargetPoint(target.getCurrentPoint());
    }

}
