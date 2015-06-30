package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.item.ItemMagicSequence;

public class ProcessFireShockwave extends ProcessExplosionBase
{

    public ProcessFireShockwave()
    {
        super(false, true);
    }

    @Override
    public String getName()
    {
        return "fire_shockwave";
    }

    @Override
    public String getTextureName()
    {
        return ItemMagicSequence.DEFAULT_ICON;
    }
    
}
