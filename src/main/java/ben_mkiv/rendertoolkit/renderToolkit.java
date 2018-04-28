package ben_mkiv.rendertoolkit;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.component.face.*;
import ben_mkiv.rendertoolkit.common.widgets.component.world.*;
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

    @SidedProxy(clientSide = "ben_mkiv.rendertoolkit.proxy.ClientProxy", serverSide = "ben_mkiv.rendertoolkit.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static renderToolkit instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        Widget.register(Cube3D.class);
        Widget.register(Box2D.class);
        Widget.register(Text2D.class);
        Widget.register(Text3D.class);
        Widget.register(Custom2D.class);
        Widget.register(Custom3D.class);
        Widget.register(Item2D.class);
        Widget.register(Item3D.class);
        Widget.register(OBJModel2D.class);
        Widget.register(OBJModel3D.class);
        Widget.register(EntityTracker3D.class);
        Widget.register(BoundingBox3D.class);

        rTkNetwork.init();

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit();
    }
}
