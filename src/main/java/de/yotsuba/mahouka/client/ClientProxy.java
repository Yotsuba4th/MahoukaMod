package de.yotsuba.mahouka.client;

import de.yotsuba.mahouka.CommonProxy;
import de.yotsuba.mahouka.magic.Target;
import de.yotsuba.mahouka.magic.cad.CadBase;
import de.yotsuba.mahouka.magic.process.MagicProcess;

public class ClientProxy extends CommonProxy
{

    @Override
    public void clientCast(MagicProcess process, CadBase cad, Target target)
    {
        process.castClient(cad, target);
    }

    @Override
    public void clientCastTick(MagicProcess process, CadBase cad, Target target)
    {
        process.castTickClient(cad, target);
    }

}
