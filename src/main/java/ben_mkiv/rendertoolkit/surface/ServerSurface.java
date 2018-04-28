package ben_mkiv.rendertoolkit.surface;

import ben_mkiv.commons0815.utils.Location;
import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.network.messages.ClientEventPacket;
import ben_mkiv.rendertoolkit.network.messages.ClientRequest;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import ben_mkiv.rendertoolkit.network.rTkNetwork;
import ben_mkiv.rendertoolkit.renderToolkit;
import ben_mkiv.rendertoolkit.server.event.ServerEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static ben_mkiv.rendertoolkit.network.messages.ClientRequest.EventType.ASYNC_SCREEN_SIZES;

public class ServerSurface {
    public static ServerEventHandler eventHandler;
    public static ServerSurface instances  = new ServerSurface();

    public HashMap<EntityPlayer, Location> players = new HashMap<>();
    public HashMap<UUID, PlayerStats> playerStats = new HashMap<>();

    public IMessage onClientEvent(ClientEventPacket message){
        Logger.getLogger(renderToolkit.MODID).info("default renderToolkit ClientEventPacket handler shouldnt be used");
        return null;
    }


    public String[] getActivePlayers(Location l){
        LinkedList<String> players = new LinkedList<String>();
        for(Map.Entry<EntityPlayer, Location> p: this.players.entrySet()){
            if(p.getValue().equals(l)){
                players.add(p.getKey().getGameProfile().getName());
            }
        }
        return players.toArray(new String[]{});
    }

    public void sendSync(EntityPlayer player, HashMap<Integer,Widget> widgetList){
        rTkNetwork.sendTo(new WidgetUpdatePacket(widgetList), player);
    }

    public void sendToUUID(WidgetUpdatePacket packet, Location UUID){
        for(Map.Entry<EntityPlayer, Location> e : players.entrySet()){
            if(e.getValue().uniqueKey.equals(UUID.uniqueKey)){
                rTkNetwork.channel.sendTo(packet, (EntityPlayerMP) e.getKey());
            }
        }
    }

    public void requestResolutionEvent(EntityPlayerMP player){
        rTkNetwork.channel.sendTo(new ClientRequest(ASYNC_SCREEN_SIZES), player);
    }

    public EntityPlayerMP checkUUID(String uuid){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
    }

    public EntityPlayerMP checkPlayerName(String name){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name);
    }
}