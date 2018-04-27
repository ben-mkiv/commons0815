package ben_mkiv.rendertoolkit;

import ben_mkiv.commons0815.utils.Location;
import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.rendertoolkit.client.renderer.ClientRenderer;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.common.widgets.component.face.*;
import ben_mkiv.rendertoolkit.common.widgets.component.world.*;
import ben_mkiv.rendertoolkit.network.messages.ClientRequest;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import ben_mkiv.rendertoolkit.network.rTkNetwork;
import ben_mkiv.rendertoolkit.proxy.CommonProxy;

import ben_mkiv.rendertoolkit.server.event.ServerEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ben_mkiv.rendertoolkit.network.messages.ClientRequest.EventType.*;


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

    public static class ClientSurface extends ClientRenderer {
        public static ClientSurface instances = new ClientSurface();

        public boolean shouldRenderStart(boolean renderingOverlay) {
            return true;
        }

        @Override
        public long getConditionStates(EntityPlayer player){ return Long.MIN_VALUE; }

        @Override
        public Vec3d getRenderOffset(){
            return new Vec3d(0, 0, 0);
        }

    }

    public static class ServerSurface {
            public static ServerEventHandler eventHandler;
            public static ServerSurface instance  = new ServerSurface();

            public HashMap<EntityPlayer, Location> players = new HashMap<>();
            public HashMap<UUID, PlayerStats> playerStats = new HashMap<>();

            /*
            public void subscribePlayer(String playerUUID, Location UUID){
                EntityPlayerMP player = checkUUID(playerUUID);
                if(player == null) return;

                OpenGlassesTerminalTileEntity terminal = UUID.getTerminal();
                if(terminal == null) return;
                if(!terminal.getTerminalUUID().equals(UUID)) return;

                players.put(player, UUID);
                playerStats.put(player.getUniqueID(), new PlayerStats(player));
                sendSync(player, UUID, terminal);

                terminal.onGlassesPutOn(player.getDisplayNameString());
                requestResolutionEvent(player);
            }

            public void unsubscribePlayer(String playerUUID){
                EntityPlayerMP p = checkUUID(playerUUID);

                Location l = players.remove(p);
                playerStats.remove(p.getUniqueID());
                if(l == null) return;

                OpenGlassesTerminalTileEntity terminal = l.getTerminal();
                if(terminal == null) return;

                terminal.onGlassesPutOff(p.getDisplayNameString());
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



            public void sendSync(EntityPlayer p, Location coords, OpenGlassesTerminalTileEntity t){
                WidgetUpdatePacket packet = new WidgetUpdatePacket(t.widgetList);
                NetworkRegistry.packetHandler.sendTo(packet, (EntityPlayerMP) p);
            }
*/


            public void sendToUUID(WidgetUpdatePacket packet, Location UUID){
                for(Map.Entry<EntityPlayer, Location> e : players.entrySet()){
                    if(e.getValue().equals(UUID)){
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
