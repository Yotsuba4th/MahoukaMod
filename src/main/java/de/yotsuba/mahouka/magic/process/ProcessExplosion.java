package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.MahoukaMod;

public class ProcessExplosion extends ProcessExplosionBase
{

    public ProcessExplosion()
    {
        super(false, true);
    }

    @Override
    public String getName()
    {
        return "explosion";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_explosion";
    }

    @Override
    public int getPsionCost()
    {
        return 75;
    }
    
}
