package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.item.ItemMagicSequence;

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
        return ItemMagicSequence.DEFAULT_ICON;
    }

}
