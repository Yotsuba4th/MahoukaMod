package de.yotsuba.mahouka;

import java.io.File;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.yotsuba.mahouka.block.BlockCadProgrammer;
import de.yotsuba.mahouka.block.BlockProcessProgrammer;
import de.yotsuba.mahouka.block.BlockSequenceProgrammer;
import de.yotsuba.mahouka.core.MahoukaEventHandler;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileEarth;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileFire;
import de.yotsuba.mahouka.entity.projectile.EntityMagicProjectileIce;
import de.yotsuba.mahouka.gui.GuiHandler;
import de.yotsuba.mahouka.item.ItemCad;
import de.yotsuba.mahouka.item.ItemMagicProcess;
import de.yotsuba.mahouka.item.ItemMagicSequence;
import de.yotsuba.mahouka.magic.MagicProcess;
import de.yotsuba.mahouka.magic.cad.CadManager;
import de.yotsuba.mahouka.magic.cast.CastingManagerClient;
import de.yotsuba.mahouka.magic.cast.CastingManagerServer;
import de.yotsuba.mahouka.network.C0PlayerData;
import de.yotsuba.mahouka.network.C2StartChanneling;
import de.yotsuba.mahouka.network.C3CancelCast;
import de.yotsuba.mahouka.network.C5CastUpdate;
import de.yotsuba.mahouka.network.S1StartChanneling;
import de.yotsuba.mahouka.network.S4CancelCast;
import de.yotsuba.mahouka.network.S6ButtonClick;

@Mod(modid = MahoukaMod.MODID, version = MahoukaMod.VERSION)
public class MahoukaMod
{

    public static final String MODID = "Mahouka";
    public static final String VERSION = "0.1";

    private static final String CONFIG_GENERAL = "General";
    private static final String CONFIG_MAGICS = "Magics";

    /* ------------------------------------------------------------ */

    @Instance(MODID)
    private static MahoukaMod instance;

    @SidedProxy(clientSide = "de.yotsuba.mahouka.client.ClientProxy", serverSide = "de.yotsuba.mahouka.CommonProxy")
    private static CommonProxy proxy;

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

    public static IIcon icon_rune_default;

    /* ------------------------------------------------------------ */

    public static final ItemCad cad = new ItemCad();

    public static final Item item_magic_sequence = new ItemMagicSequence();

    /* ------------------------------------------------------------ */

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
        registerEntity(EntityMagicProjectileFire.class, "magic_projectile_fire");
        registerEntity(EntityMagicProjectileIce.class, "magic_projectile_ice");
        registerEntity(EntityMagicProjectileEarth.class, "magic_projectile_earth");
    }

    public static int registerEntity(Class<? extends Entity> entityClass, String name)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
        EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 1, true);
        return entityID;
    }

    @SuppressWarnings("unchecked")
    public static int registerMob(Class<? extends Entity> entityClass, String name)
    {
        int entityID = registerEntity(entityClass, name);
        long seed = name.hashCode();
        Random rand = new Random(seed);
        int primaryColor = rand.nextInt() * 16777215;
        int secondaryColor = rand.nextInt() * 16777215;
        EntityList.entityEggs.put(entityID, new EntityList.EntityEggInfo(entityID, primaryColor, secondaryColor));
        return entityID;
    }

    private void registerItems()
    {
        GameRegistry.registerItem(cad, "cad");
        GameRegistry.registerItem(item_magic_sequence, "magic_sequence");
        for (Short id : MagicProcess.processById.keySet())
        {
            MagicProcess process = MagicProcess.createById(id);
            ItemMagicProcess item = new ItemMagicProcess(process);
            GameRegistry.registerItem(item, process.getItemName());
            MagicProcess.registerProcessItem(process.getClass(), item);
            process.registerIcons();
        }
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
        castingManagerServer.serverStoppedEvent(event);
    }

    /* ------------------------------------------------------------ */

    public static MahoukaMod getInstance()
    {
        return instance;
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

    public static CastingManagerServer getCastingManagerServer()
    {
        return castingManagerServer;
    }

    public static CastingManagerClient getCastingManagerClient()
    {
        return castingManagerClient;
    }

}
