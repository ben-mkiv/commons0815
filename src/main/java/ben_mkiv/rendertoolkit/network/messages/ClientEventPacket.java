package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.network.EventType;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import ben_mkiv.rendertoolkit.surface.ServerSurface;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientEventPacket implements IMessage {
    public  int pentid = -1, dimId = -1, targetId = -1;
    public  BlockPos pos = new BlockPos(0, 0, 0);
    public  Vec3d renderOffset = new Vec3d(0, 0, 0);
    public  Vec3d resolution = new Vec3d(0, 0, 0);
    public EventType type;
    public EnumHand hand;

    public int x=-1,y=-1,mb=-1;

    public ClientEventPacket() {}

    public ClientEventPacket(EventType type, PlayerInteractEvent event){
        this(type);

        EntityPlayer player = event.getEntityPlayer();

        this.pentid = player.getEntityId();
        this.dimId = player.dimension;
        this.pos = event.getPos();
        this.hand = event.getHand();
    }

    public ClientEventPacket(EventType type) {
        this.type = type;
    }

    @SideOnly(Side.CLIENT)
    public ClientEventPacket(EventType type, Vec3d renderOffset) {
        this(type);
        EntityPlayer player = Minecraft.getMinecraft().player;

        this.pentid = player.getEntityId();
        this.dimId = player.dimension;
        this.renderOffset = renderOffset;
    }

    public ClientEventPacket(EventType type, EntityPlayer player, Vec3d mouseData){
        this(type);
        this.pentid = player.getEntityId();
        this.dimId = player.dimension;
        this.x = (int) Math.round(mouseData.x);
        this.y = (int) Math.round(mouseData.y);
        this.mb = (int) mouseData.z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = EventType.values()[buf.readInt()];

        this.pentid = buf.readInt();
        this.dimId = buf.readInt();
        this.targetId = buf.readInt();

        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        switch(this.type){
            case GLASSES_SCREEN_SIZE:
                this.resolution = new Vec3d(buf.readInt(), buf.readInt(), buf.readInt());
                this.renderOffset = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
                break;
            case INTERACT_OVERLAY:
                this.x = buf.readInt();
                this.y = buf.readInt();
                this.mb = buf.readInt();
                break;
            case INTERACT_WORLD_RIGHT:
            case INTERACT_WORLD_LEFT:
            case INTERACT_WORLD_BLOCK_RIGHT:
            case INTERACT_WORLD_BLOCK_LEFT:
                this.hand = EnumHand.values()[buf.readInt()];
                break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.type.ordinal());

        buf.writeInt(this.pentid);
        buf.writeInt(this.dimId);
        buf.writeInt(this.targetId);

        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());

        switch(this.type){
            case GLASSES_SCREEN_SIZE:
                buf.writeInt(ClientSurface.resolution.getScaledWidth());
                buf.writeInt(ClientSurface.resolution.getScaledHeight());
                buf.writeInt(ClientSurface.resolution.getScaleFactor());
                buf.writeDouble(this.renderOffset.x);
                buf.writeDouble(this.renderOffset.y);
                buf.writeDouble(this.renderOffset.z);
                break;
            case INTERACT_OVERLAY:
                buf.writeInt(this.x);
                buf.writeInt(this.y);
                buf.writeInt(this.mb);
                break;
            case INTERACT_WORLD_RIGHT:
            case INTERACT_WORLD_LEFT:
            case INTERACT_WORLD_BLOCK_RIGHT:
            case INTERACT_WORLD_BLOCK_LEFT:
                buf.writeInt(this.hand.ordinal());
                break;
        }
    }

    public static class Handler implements IMessageHandler<ClientEventPacket, IMessage> {

        @Override
        public IMessage onMessage(ClientEventPacket message, MessageContext ctx) {
            return ServerSurface.instances.onClientEvent(message);
        }
    }

}