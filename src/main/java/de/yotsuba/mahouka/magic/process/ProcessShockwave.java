package de.yotsuba.mahouka.magic.process;


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
        return DEFAULT_ICON;
    }

}
