package ben_mkiv.rendertoolkit;

import ben_mkiv.rendertoolkit.network.rTkNetwork;
import ben_mkiv.rendertoolkit.proxy.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = renderToolkit.MODID, version = renderToolkit.VERSION)
public class renderToolkit
{
    public static final String MODID = "rendertoolkit";
    public static final String VERSION = "@VERSION@";

    public static boolean Optifine = false;
    public static boolean Albedo = false;

    @SidedProxy(clientSide = "ben_mkiv.rendertoolkit.proxy.ClientProxy", serverSide = "ben_mkiv.rendertoolkit.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(value = MODID)
    public static renderToolkit instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        rTkNetwork.init();

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit();
    }

}
