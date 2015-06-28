package de.yotsuba.mahouka.client;

import de.yotsuba.mahouka.CommonProxy;
import de.yotsuba.mahouka.magic.CastingProcess;
import de.yotsuba.mahouka.magic.Target;
import de.yotsuba.mahouka.magic.process.MagicProcess;

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
