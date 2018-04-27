package ben_mkiv.guitoolkit.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.ArrayList;

public class guiHandler {
    public static ArrayList<GUIScreen> screens = new ArrayList<>();
    private static int screenIndex = 0;

    public static EntityPlayer player;

    public static void update(EntityPlayer player){
        guiHandler.player = player;
    }

    public static class GUIScreen{
        public Class<? extends guiWindow> client;
        public String clientClassname;
        public Class<? extends Container> server;
        int index;

        public GUIScreen(int index, Class client, String cName, Class server){
            this.server = server;
            this.client = client;
            this.index = index;
            this.clientClassname = cName;
        }
    }

    public static void register(Class client, Class server){
        screens.add(new GUIScreen(screenIndex++, client, client.getSimpleName(), server));
    }

    public static void register(String clientClassName, Class server){
        screens.add(new GUIScreen(screenIndex++, dummyGUI.class, clientClassName, server));
    }

    public static int getIndex(Class c){
        for(GUIScreen cS : screens){
            if(cS.server.equals(c))
                return cS.index;
            if(cS.client.equals(c))
                return cS.index;
        }

        return -1;
    }

    public static int getIndex(String c){
        for(GUIScreen cS : screens){
            if(cS.clientClassname.equals(c))
                return cS.index;
        }

        return -1;
    }

    public static Class<? extends Container> getClassServer(int index){
        for(GUIScreen screen : screens)
            if(screen.index == index)
                return screen.server;

        return null;
    }

    public static Class<? extends guiWindow> getClassClient(int index){
        for(GUIScreen screen : screens)
            if(screen.index == index)
                return screen.client;

        return null;
    }

}
