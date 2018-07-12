package ben_mkiv.guitoolkit.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.logging.Logger;

public class guiHandler {
    public static HashMap<String, GUIScreen> screens = new HashMap<>();
    protected static int screenIndex = 0;

    public static EntityPlayer player;

    public static void update(EntityPlayer player){
        guiHandler.player = player;
    }

    public static class GUIScreen{
        public Class<? extends guiWindow> client;
        public String clientClassname;
        public Class<? extends Container> server;
        public int index;

        public GUIScreen(int index, Class client, String cName, Class server){
            this.server = server;
            this.client = client;
            this.index = index;
            this.clientClassname = cName;
        }
    }

    public class dummyGUI {
        public dummyGUI() {
        }
    }

    public static void register(Class client, Class server){
        screens.put(client.getSimpleName(), new GUIScreen(screenIndex++, client, client.getSimpleName(), server));
    }

    public static void register(String clientClassName, Class server){
        Class clientClass = dummyGUI.class;


        if(FMLCommonHandler.instance().getEffectiveSide().isClient()) try {
            clientClass = server.getClassLoader().loadClass(clientClassName).getClass();
        } catch (Exception ex) {
            Logger.getLogger("commons0815").warning("cant find class: " + clientClassName);
        }

        screens.put(clientClassName, new GUIScreen(screenIndex++, clientClass, clientClassName, server));
    }

    public static int getIndex(Class c){
        for(GUIScreen cS : screens.values()){
            if(cS.server.equals(c))
                return cS.index;
            if(cS.client.equals(c))
                return cS.index;
        }

        return -1;
    }

    public static int getIndex(String c){
        return screens.get(c).index;
    }

    public static Class<? extends Container> getClassServer(int index){
        for(GUIScreen screen : screens.values())
            if(screen.index == index)
                return screen.server;

        return null;
    }

    public static Class<? extends guiWindow> getClassClient(int index){
        for(GUIScreen screen : screens.values())
            if(screen.index == index)
                return screen.client;

        return null;
    }

}
