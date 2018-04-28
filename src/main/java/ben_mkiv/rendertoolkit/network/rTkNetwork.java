package ben_mkiv.rendertoolkit.network;

import ben_mkiv.rendertoolkit.network.messages.ClientEventPacket;
import ben_mkiv.rendertoolkit.network.messages.ClientRequest;
import ben_mkiv.rendertoolkit.network.messages.WidgetUpdatePacket;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class rTkNetwork {

    public static SimpleNetworkWrapper channel;
//
    static int id = -1;

    public static void init(){
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(renderToolkit.MODID.toLowerCase());

        registerMessage(ClientEventPacket.Handler.class, ClientEventPacket.class, Side.SERVER);
        registerMessage(ClientRequest.Handler.class, ClientRequest.class, Side.CLIENT);
        registerMessage(WidgetUpdatePacket.Handler.class, WidgetUpdatePacket.class, Side.CLIENT);
    }

    public static void registerMessage(Class handler, Class clazz, Side side){
        channel.registerMessage(handler, clazz, id++, side);
    }

    public static void sendToTrackingPlayers(IMessage msg, Entity entity){
        if(entity == null)
            return;

        if(entity.world.isRemote)
            return;

        if(renderToolkit.proxy.isServer() && entity instanceof EntityPlayerMP && FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().contains(entity))
            sendTo(msg, (EntityPlayerMP) entity);

        for(EntityPlayer p : DimensionManager.getWorld(entity.dimension).getEntityTracker().getTrackingPlayers(entity))
            sendTo(msg, p);
    }

    public static void sendToNearPlayers(IMessage msg, TileEntity entity){
        sendToNearPlayers(msg, entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getX(), entity.getWorld());
    }

    public static void sendToNearPlayers(IMessage msg, double x, double y, double z, World world){
        if(msg == null)
            return;

        if(world == null)
            return;

        if(channel == null)
            return;

        int dimension = world.provider.getDimension();

        PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        int viewDistance = players.getViewDistance()*16;

        for(EntityPlayerMP player : players.getPlayers())
            if(player != null && dimension == player.dimension)
               if(player.getDistance(x, y, z) <= viewDistance)
                    channel.sendTo(msg, player);
    }

    public static void sendTo(IMessage msg, EntityPlayer player){
        channel.sendTo(msg, (EntityPlayerMP) player);
    }

    public static void sendToNearPlayers(IMessage msg, Entity entity){
        sendToNearPlayers(msg, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), entity.world);
    }


}