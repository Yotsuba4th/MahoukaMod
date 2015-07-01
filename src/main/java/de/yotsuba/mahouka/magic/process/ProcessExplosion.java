package de.yotsuba.mahouka.magic.process;


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
        return DEFAULT_ICON;
    }

}
