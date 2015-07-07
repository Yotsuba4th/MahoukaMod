package de.yotsuba.mahouka;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.core.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.block.BlockCadProgrammer;
import de.yotsuba.mahouka.block.BlockProcessProgrammer;
import de.yotsuba.mahouka.block.BlockSequenceProgrammer;
import de.yotsuba.mahouka.core.MahoukaEventHandler;
import de.yotsuba.mahouka.core.PlayerMotionTracker;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileEarth;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileFire;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileIce;
import de.yotsuba.mahouka.gui.GuiHandler;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingManager;
import de.yotsuba.mahouka.magic.process.ProcessAccelerate;
import de.yotsuba.mahouka.magic.process.ProcessDecomposition;
import de.yotsuba.mahouka.magic.process.ProcessExplosion;
import de.yotsuba.mahouka.magic.process.ProcessFireShockwave;
import de.yotsuba.mahouka.magic.process.ProcessFirebomb;
import de.yotsuba.mahouka.magic.process.ProcessMovingOffset;
import de.yotsuba.mahouka.magic.process.ProcessOffset;
import de.yotsuba.mahouka.magic.process.ProcessOffsetAhead;
import de.yotsuba.mahouka.magic.process.ProcessParallel;
import de.yotsuba.mahouka.magic.process.ProcessParticle;
import de.yotsuba.mahouka.magic.process.ProcessProjectileFire;
import de.yotsuba.mahouka.magic.process.ProcessProjectileIce;
import de.yotsuba.mahouka.magic.process.ProcessSequence;
import de.yotsuba.mahouka.magic.process.ProcessShockwave;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.network.C2StartChanneling;
import de.yotsuba.mahouka.network.C3CancelCast;
import de.yotsuba.mahouka.network.C5CastUpdate;
import de.yotsuba.mahouka.network.S1StartChanneling;
import de.yotsuba.mahouka.network.S4CancelCast;
import de.yotsuba.mahouka.network.S6ButtonClick;
import de.yotsuba.mahouka.util.Utils;

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

    public static boolean DEBUG = false;

    @SidedProxy(clientSide = "de.yotsuba.mahouka.client.ClientProxy", serverSide = "de.yotsuba.mahouka.CommonProxy")
    private static CommonProxy proxy;

    private static SimpleNetworkWrapper netChannel = NetworkRegistry.INSTANCE.newSimpleChannel("mahouka");

    private static CadManager cadManager = new CadManager();

    @SuppressWarnings("unused")
    private static CastingManager castingManager = new CastingManager();

    @SuppressWarnings("unused")
    private static MahoukaEventHandler eventHandler = new MahoukaEventHandler();

    @SuppressWarnings("unused")
    private static PlayerMotionTracker motionTracker = new PlayerMotionTracker();

    private static Logger logger;

    /* ------------------------------------------------------------ */

    public static final CreativeTabs creativeTab = new CreativeTabs("Mahouka") {
        @Override
        public Item getTabIconItem()
        {
            return cad;
        }
    };

    /* ------------------------------------------------------------ */

    public static IIcon icon_rune_default;

    /* ------------------------------------------------------------ */

    public static final ItemCad cad = new ItemCad();

    public static ItemMagicProcess item_magic_sequence;

    /* ------------------------------------------------------------ */

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = (Logger) event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        loadConfig();
        registerItems();
        registerBlocks();
        registerEntites();
        registerNetworkMessages();
        proxy.init(event);
    }

    private void loadConfig()
    {
        Configuration config = new Configuration(new File("config/" + MODID + ".cfg"), true);
        config.setCategoryComment(CONFIG_GENERAL, "General mod config");
        config.setCategoryComment(CONFIG_MAGICS, "Enable or disable certain magics");
        config.save();
    }

    private void registerEntites()
    {
        Utils.registerEntity(EntityMagicProjectileFire.class, "magic_projectile_fire");
        Utils.registerEntity(EntityMagicProjectileIce.class, "magic_projectile_ice");
        Utils.registerEntity(EntityMagicProjectileEarth.class, "magic_projectile_earth");
    }

    private void registerItems()
    {
        GameRegistry.registerItem(cad, "cad");

        List<MagicProcess> processes = new ArrayList<MagicProcess>();
        processes.add(new ProcessSequence());
        processes.add(new ProcessParallel());
        
        processes.add(new ProcessOffset());
        processes.add(new ProcessOffsetAhead());
        processes.add(new ProcessMovingOffset());

        processes.add(new ProcessAccelerate());
        processes.add(new ProcessParticle());
        processes.add(new ProcessDecomposition());
        
        processes.add(new ProcessProjectileFire());
        processes.add(new ProcessProjectileIce());
        
        processes.add(new ProcessExplosion());
        processes.add(new ProcessShockwave());
        processes.add(new ProcessFirebomb());
        processes.add(new ProcessFireShockwave());
        
        for (MagicProcess process : processes)
        {
            ItemMagicProcess item = process.createItem();
            GameRegistry.registerItem(item, process.getItemName());
            MagicProcess.registerProcessItem(process.getClass(), item);
        }

        item_magic_sequence = MagicProcess.itemByProcess.get(ProcessSequence.class);
        item_magic_sequence.setCreativeTab(null);
    }

    private void registerBlocks()
    {
        GameRegistry.registerBlock(BlockProcessProgrammer.BLOCK, BlockProcessProgrammer.ID);
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
        netChannel.registerMessage(S6ButtonClick.class, S6ButtonClick.class, 6, Side.SERVER);
    }

    /* ------------------------------------------------------------ */

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void textureStitchEvent(TextureStitchEvent event)
    {
        icon_rune_default = event.map.registerIcon(MahoukaMod.MODID + ":rune_default");
    }

    /* ------------------------------------------------------------ */

    @EventHandler
    public void serverStoppedEvent(FMLServerStoppedEvent event)
    {
        CadManager.serverStoppedEvent(event);
        CastingManager.serverStoppedEvent();
    }

    /* ------------------------------------------------------------ */

    public static MahoukaMod getInstance()
    {
        return instance;
    }

    public static org.apache.logging.log4j.Logger getLogger()
    {
        return logger;
    }

    public static CommonProxy getProxy()
    {
        return proxy;
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
