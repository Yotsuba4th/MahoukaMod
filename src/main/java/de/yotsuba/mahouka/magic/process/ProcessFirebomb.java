package de.yotsuba.mahouka.magic.process;


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
        return DEFAULT_ICON;
    }

}
