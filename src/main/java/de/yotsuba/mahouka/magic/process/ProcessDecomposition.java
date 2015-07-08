package de.yotsuba.mahouka.magic.process;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.Utils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;
import de.yotsuba.mahouka.util.target.TargetPoint;

public class ProcessDecomposition extends MagicProcess
{

    public static final String NAME = "decomposition";

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public boolean isTargetValid(Target target)
    {
        if (target instanceof TargetBlock)
        {
            // TODO (5) Check if block breakable
            return true;
        }
        if (target instanceof TargetEntity)
        {
            Entity entity = ((TargetEntity) target).getEntity();
            if (entity instanceof EntityPlayer)
                return false;
            return !entity.isEntityInvulnerable();
        }
        return false;
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
        // DamageSource dmgSource = new EntityDamageSource(NAME, cp.getCaster()).setMagicDamage().setDamageBypassesArmor().setDamageIsAbsolute();
        if (target instanceof TargetEntity)
        {
            Entity entity = ((TargetEntity) target).getEntity();
            entity.setDead();
        }
        else if (target instanceof TargetBlock)
        {
            TargetBlock block = (TargetBlock) target;
            // TODO (5) Trigger BlockBreak event and check if block breakable
            if (block.getBlock().getBlockHardness(null, 0, 0, 0) >= 0)
            {
                cp.getWorld().setBlock(block.getX(), block.getY(), block.getZ(), Blocks.air);
            }
        }

        return new TargetPoint(target.getCurrentPoint());
    }

}
