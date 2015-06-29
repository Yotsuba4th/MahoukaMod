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
import cpw.mods.fml.relauncher.Side;
import de.yotsuba.mahouka.block.BlockCadProgrammer;
import de.yotsuba.mahouka.block.BlockProcessAssembler;
import de.yotsuba.mahouka.block.BlockSequenceProgrammer;
import de.yotsuba.mahouka.core.MahoukaEventHandler;
import de.yotsuba.mahouka.gui.GuiHandler;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingManagerClient;
import de.yotsuba.mahouka.magic.cast.CastingManagerServer;
import de.yotsuba.mahouka.magic.process.ProcessExplosion;
import de.yotsuba.mahouka.magic.process.ProcessParticle;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.network.C2StartChanneling;
import de.yotsuba.mahouka.network.C3CancelCast;
import de.yotsuba.mahouka.network.C5CastUpdate;
import de.yotsuba.mahouka.network.S1StartChanneling;
import de.yotsuba.mahouka.network.S4CancelCast;

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

    private static CastingManagerServer castingManagerServer = new CastingManagerServer();

    private static CastingManagerClient castingManagerClient = new CastingManagerClient();

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

    /* ------------------------------------------------------------ */

    public static final ItemCad cad = new ItemCad();

    public static final Item item_magic_sequence = new ItemMagicSequence();

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
        CadManager.serverStoppedEvent(event);
        castingManagerServer.serverStoppedEvent(event);
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
        GameRegistry.registerItem(item_magic_sequence, "magic_sequence");
        registerItem(new ItemMagicProcess(new ProcessParticle()), "process_particle");
        registerItem(new ItemMagicProcess(new ProcessExplosion(false, true)), "process_explosion");
        registerItem(new ItemMagicProcess(new ProcessExplosion(true, true)), "process_firebomb");
        registerItem(new ItemMagicProcess(new ProcessExplosion(false, false)), "process_shockwave");
        registerItem(new ItemMagicProcess(new ProcessExplosion(true, false)), "process_fire_shockwave");
    }

    private void registerItem(ItemMagicProcess imp, String name)
    {
        GameRegistry.registerItem(imp.setUnlocalizedName(name), name);
    }

    private void registerBlocks()
    {
        GameRegistry.registerBlock(BlockProcessAssembler.BLOCK, BlockProcessAssembler.ID);
        GameRegistry.registerBlock(BlockSequenceProgrammer.BLOCK, BlockSequenceProgrammer.ID);
        GameRegistry.registerBlock(BlockCadProgrammer.BLOCK, BlockCadProgrammer.ID);
    }

    private void registerNetworkMessages()
    {
        netChannel.registerMessage(C0PlayerData.class, C0PlayerData.class, 0, Side.CLIENT);
        netChannel.registerMessage(S1StartChanneling.class, S1StartChanneling.class, 1, Side.SERVER);
        netChannel.registerMessage(C2StartChanneling.class, C2StartChanneling.class, 2, Side.CLIENT);
        netChannel.registerMessage(C3CancelCast.class, C3CancelCast.class, 3, Side.CLIENT);
        netChannel.registerMessage(S4CancelCast.class, S4CancelCast.class, 4, Side.SERVER);
        netChannel.registerMessage(C5CastUpdate.class, C5CastUpdate.class, 5, Side.CLIENT);
    }

    public static CadManager getCadManager()
    {
        return cadManager;
    }

    public static SimpleNetworkWrapper getNetChannel()
    {
        return netChannel;
    }

    public static CastingManagerServer getCastingManagerServer()
    {
        return castingManagerServer;
    }

    public static CastingManagerClient getCastingManagerClient()
    {
        return castingManagerClient;
    }

}
