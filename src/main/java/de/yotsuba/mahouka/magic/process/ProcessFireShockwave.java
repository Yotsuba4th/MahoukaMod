package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.MahoukaMod;

public class ProcessFireShockwave extends ProcessExplosionBase
{

    public ProcessFireShockwave()
    {
        super(true, false);
    }

    @Override
    public String getName()
    {
        return "fire_shockwave";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_fire_shockwave";
    }

}
