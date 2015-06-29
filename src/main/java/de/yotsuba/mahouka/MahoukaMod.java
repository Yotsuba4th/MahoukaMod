package de.yotsuba.mahouka;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import de.yotsuba.mahouka.block.BlockCadAssembler;
import cpw.mods.fml.relauncher.Side;
import de.yotsuba.mahouka.core.MahoukaEventHandler;
import de.yotsuba.mahouka.gui.GuiHandler;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.magic.CadManager;
import de.yotsuba.mahouka.magic.CastingManager;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.network.C1StartChanneling;

@Mod(modid = MahoukaMod.MODID, version = MahoukaMod.VERSION)
public class MahoukaMod
{

    public static final String MODID = "Mahouka";
    public static final String VERSION = "0.1";

    private static final String CONFIG_GENERAL = "General";
    private static final String CONFIG_MAGICS = "Magics";

    /* ------------------------------------------------------------ */

    @Instance(MODID)
    public static MahoukaMod instance;

    @SidedProxy(clientSide = "de.yotsuba.mahouka.client.ClientProxy", serverSide = "de.yotsuba.mahouka.CommonProxy")
    public static CommonProxy proxy;

    private static SimpleNetworkWrapper netChannel = NetworkRegistry.INSTANCE.newSimpleChannel("mahouka");

    private static CadManager cadManager = new CadManager();

    @SuppressWarnings("unused")
    private static MahoukaEventHandler eventHandler = new MahoukaEventHandler();

    /* ------------------------------------------------------------ */

    public static final CreativeTabs creativeTab = new CreativeTabs("Mahouka") {
        @Override
        public Item getTabIconItem()
        {
            return cad;
        }
    };

    public static final ItemCad cad = new ItemCad();

    /* ------------------------------------------------------------ */

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        loadConfig();
        registerItems();
        registerBlocks();
        registerNetworkMessages();
        proxy.init(event);
    }

    @EventHandler
    public void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        cadManager.serverStoppedEvent(event);
        CastingManager.serverStoppedEvent(event);
    }

    private void loadConfig()
    {
        Configuration config = new Configuration(new File("config/" + MODID + ".cfg"), true);
        config.setCategoryComment(CONFIG_GENERAL, "General mod config");
        config.setCategoryComment(CONFIG_MAGICS, "Enable or disable certain magics");
        config.save();
    }

    private void registerItems()
    {
        GameRegistry.registerItem(cad, "cad");
    }

    private void registerBlocks()
    {
        GameRegistry.registerBlock(BlockCadAssembler.block, "cad_assembler");
    }

    private void registerNetworkMessages()
    {
        netChannel.registerMessage(C0PlayerData.class, C0PlayerData.class, 0, Side.CLIENT);
        netChannel.registerMessage(C1StartChanneling.class, C1StartChanneling.class, 1, Side.CLIENT);
    }

    public static CadManager getCadManager()
    {
        return cadManager;
    }

    public static SimpleNetworkWrapper getNetChannel()
    {
        return netChannel;
    }

}
