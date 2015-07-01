package de.yotsuba.mahouka.magic;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public abstract class MagicProcessBase extends MagicProcess
{

    protected IIcon icon;

    public abstract String getTextureName();

    @Override
    public IIcon getIcon()
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister iconReg)
    {
        icon = iconReg.registerIcon(getTextureName());
    }

}
