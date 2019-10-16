package ben_mkiv.guitoolkit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = guiToolkit.MODID, version = guiToolkit.VERSION)
public class guiToolkit
{
    public static final String MODID = "guitoolkit";
    public static final String VERSION = "@VERSION@";

    @Mod.Instance(MODID)
    public static guiToolkit instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
    }
}
