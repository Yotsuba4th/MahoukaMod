package de.yotsuba.mahouka.magic.process;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.MathUtils;
import de.yotsuba.mahouka.util.target.Target;
import de.yotsuba.mahouka.util.target.TargetBlock;
import de.yotsuba.mahouka.util.target.TargetEntity;

public class ProcessDestabilize extends MagicProcess
{

    @Override
    public String getName()
    {
        return "destabilize";
    }

    @Override
    public boolean isTargetValid(Target target)
    {
        return target instanceof TargetBlock;
    }

    @Override
    public int getPsionCost()
    {
        return 2;
    }

    @Override
    public int getChannelingDuration()
    {
        return 0;
    }

    @Override
    public int getCastDuration(Target target)
    {
        // TargetBlock t = (TargetBlock) target;
        // int hardness = t.getBlock().getBlockHardness(t.getWorld(), t.getX(), t.getY(), t.getZ());
        return 2;
    }

    /* ------------------------------------------------------------ */

    @Override
    @SideOnly(Side.CLIENT)
    public void castTickClient(CastingProcess cp, Target target)
    {
        Vec3 point = target.getCurrentPoint();
        double scale = 0.5;
        double x = point.xCoord + MathUtils.rand.nextGaussian() * scale;
        double y = point.yCoord + MathUtils.rand.nextGaussian() * scale;
        double z = point.zCoord + MathUtils.rand.nextGaussian() * scale;
        target.getWorld().spawnParticle("cloud", x, y, z, 0, 0, 0);
    }

    @Override
    public Target castEnd(CastingProcess cp, Target target)
    {
        TargetBlock t = (TargetBlock) target;
        EntityFallingBlock entity = new EntityFallingBlock(target.getWorld(), t.getX() + 0.5, t.getY() + 0.5, t.getZ() + 0.5, t.getBlock());
        entity.worldObj.spawnEntityInWorld(entity);
        return new TargetEntity(entity, false, false);
    }

}
