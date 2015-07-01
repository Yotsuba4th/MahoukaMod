package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.MahoukaMod;


public class ProcessFirebomb extends ProcessExplosionBase
{

    public ProcessFirebomb()
    {
        super(true, true);
    }

    @Override
    public String getName()
    {
        return "firebomb";
    }

    @Override
    public String getTextureName()
    {
        return MahoukaMod.MODID + ":process_firebomb";
    }

}
