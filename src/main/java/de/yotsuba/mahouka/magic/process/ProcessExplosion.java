package de.yotsuba.mahouka.magic.process;

import de.yotsuba.mahouka.item.ItemMagicSequence;

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
        // return MahoukaMod.MODID + ":cad";
        return ItemMagicSequence.DEFAULT_ICON;
    }

}
