package de.yotsuba.mahouka.client;

import de.yotsuba.mahouka.CommonProxy;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cast.CastingProcess;
import de.yotsuba.mahouka.util.target.Target;

public class ClientProxy extends CommonProxy
{

    @Override
    public void clientCast(MagicProcess process, CastingProcess cp, Target target)
    {
        process.castClient(cp, target);
    }

    @Override
    public void clientCastTick(MagicProcess process, CastingProcess cp, Target target)
    {
        process.castTickClient(cp, target);
    }

}
