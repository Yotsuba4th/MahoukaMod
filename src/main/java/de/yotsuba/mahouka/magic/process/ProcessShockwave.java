package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.MahoukaMod;

public class ProcessShockwave extends ProcessExplosionBase
{

    public ProcessShockwave()
    {
        super(false, false);
    }

    @Override
    public String getName()
    {
        return "shockwave";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_shockwave";
    }

    @Override
    public int getPsionCost()
    {
        return 40;
    }
    
}
