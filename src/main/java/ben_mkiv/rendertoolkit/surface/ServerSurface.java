package ben_mkiv.rendertoolkit.surface;

import ben_mkiv.commons0815.utils.Location;
import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import ben_mkiv.rendertoolkit.network.rTkNetwork;
import ben_mkiv.rendertoolkit.server.event.ServerEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class ServerSurface {
    public static ServerEventHandler eventHandler;
    public static ServerSurface instances  = new ServerSurface();

    //first UUID is the player UUID, hashset is a list of the connected hosts
    public HashMap<UUID, HashSet<UUID>> players = new HashMap<>();
    public HashMap<UUID, PlayerStats> playerStats = new HashMap<>();

    public String[] getActivePlayers(UUID uuid){
        LinkedList<String> players = new LinkedList<String>();
        for(Map.Entry<UUID, HashSet<UUID>> p: this.players.entrySet()){
            if(p.getValue().contains(uuid)){
                players.add(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(p.getKey()).getDisplayName().getUnformattedText());
            }
        }
        return players.toArray(new String[]{});
    }

    public void sendSync(UUID uuid, EntityPlayer player, HashMap<Integer,Widget> widgetList){
        if(player != null)
            rTkNetwork.sendTo(new WidgetUpdatePacket(uuid, widgetList), player);
    }

    @Deprecated
    public void sendToUUID(WidgetUpdatePacket packet, Location UUID){
        for(Map.Entry<UUID, HashSet<UUID>> e : players.entrySet()){
            if(e.getValue().contains(UUID.uniqueKey)){
                EntityPlayerMP player = checkUUID(e.getKey());
                if(player != null)
                    rTkNetwork.channel.sendTo(packet, player);
            }
        }
    }

    public void sendToUUID(WidgetUpdatePacket packet, UUID uuid){
        if(uuid == null)
            return;

        for(Map.Entry<UUID, HashSet<UUID>> e : players.entrySet()){
            if(e.getValue().contains(uuid)){
                EntityPlayerMP player = checkUUID(e.getKey());
                if(player != null)
                    rTkNetwork.channel.sendTo(packet, player);
            }
        }
    }

    HashMap<UUID, EntityPlayerMP> playerCache = new HashMap<>();

    public EntityPlayerMP checkUUID(UUID uuid){
        if(!playerCache.containsKey(uuid) || playerCache.get(uuid) == null) {
            EntityPlayerMP player = null;
            for(EntityPlayerMP playerDB : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
                if(uuid.equals(playerDB.getUniqueID())) {
                    playerCache.put(uuid, player);
                    return playerDB;
                }

            return player;
        }

        return playerCache.get(uuid);
    }
}