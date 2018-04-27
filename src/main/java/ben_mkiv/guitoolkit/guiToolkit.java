package ben_mkiv.guitoolkit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = guiToolkit.MODID, version = guiToolkit.VERSION)
public class guiToolkit
{
    public static final String MODID = "guitoolkit";
    public static final String VERSION = "@VERSION@";

    public static guiToolkit instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    }
}
