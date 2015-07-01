package de.yotsuba.mahouka.magic.process;


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
        return DEFAULT_ICON;
    }
    
}
