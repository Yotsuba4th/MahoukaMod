package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.item.ItemMagicSequence;

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
        return ItemMagicSequence.DEFAULT_ICON;
    }

}
