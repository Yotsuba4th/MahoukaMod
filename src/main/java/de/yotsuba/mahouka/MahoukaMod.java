package de.yotsuba.mahouka;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import de.yotsuba.mahouka.gui.GuiHandler;

@Mod(modid = MahoukaMod.MODID, version = MahoukaMod.VERSION)
public class MahoukaMod
{

    public static final String MODID = "Mahouka";
    public static final String VERSION = "0.1";

    private static final String CONFIG_GENERAL = "General";
    private static final String CONFIG_MAGICS = "Magics";

    /* ------------------------------------------------------------ */

    @Instance(MODID)
    public static Mod instance;

    @SidedProxy(clientSide = "de.yotsuba.mahouka.ClientProxy", serverSide = "de.yotsuba.mahouka.CommonProxy")
    public static CommonProxy proxy;

    /* ------------------------------------------------------------ */

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        loadConfig();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        FMLCommonHandler.instance().bus().register(this);
        proxy.init(event);
    }

    private void loadConfig()
    {
        Configuration config = new Configuration(new File("config/" + MODID + ".cfg"), true);
        config.setCategoryComment(CONFIG_GENERAL, "General mod config");
        config.setCategoryComment(CONFIG_MAGICS, "Enable or disable certain magics");
        config.save();
    }

}
