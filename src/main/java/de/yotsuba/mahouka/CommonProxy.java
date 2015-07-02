package de.yotsuba.mahouka;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;

public class CommonProxy
{

    public void castStartClient(MagicProcess process, CastingProcess cp, Target target)
    {
        /* do nothing */
    }

    public void castTickClient(MagicProcess process, CastingProcess cp, Target target)
    {
        /* do nothing */
    }

    public void castEndClient(MagicProcess process, CastingProcess cp, Target target)
    {
        /* do nothing */
    }

    /* ------------------------------------------------------------ */

    public void init(FMLInitializationEvent event)
    {
        /* do nothing */
    }

}
