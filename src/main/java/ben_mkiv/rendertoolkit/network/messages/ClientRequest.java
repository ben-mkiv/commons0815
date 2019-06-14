package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.surface.ClientSurface;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class ClientRequest implements IMessage {
    public enum EventType{
        SYNC_SCREEN_SIZE,
        ASYNC_SCREEN_SIZES,
        SET_RENDER_RESOLUTION
    }

    protected double width,height,scale;

    protected EventType type;
    private UUID instanceUUID = null;

    public ClientRequest() {}

    //SET_RENDER_RESOLUTION
    public ClientRequest(EventType type, UUID instanceUUID, double width, double height, double scale) {
        this(type, instanceUUID);
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    //SYNC_SCREEN_SIZE, ASYNC_SCREEN_SIZES
    public ClientRequest(EventType type, UUID instanceUUID) {
        this.type = type;
        this.instanceUUID = instanceUUID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if(buf.readBoolean())
            instanceUUID = new UUID(buf.readLong(), buf.readLong());

        this.type = EventType.values()[buf.readInt()];

        if(this.type.equals(EventType.SET_RENDER_RESOLUTION)){
            this.width = buf.readDouble();
            this.height = buf.readDouble();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(instanceUUID != null);
        if(instanceUUID != null){
            buf.writeLong(instanceUUID.getMostSignificantBits());
            buf.writeLong(instanceUUID.getLeastSignificantBits());
        }

        buf.writeInt(this.type.ordinal());

        if(this.type.equals(EventType.SET_RENDER_RESOLUTION)){
            buf.writeDouble(this.width);
            buf.writeDouble(this.height);
        }
    }

    public static class Handler implements IMessageHandler<ClientRequest, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(ClientRequest message, MessageContext ctx) {

            switch(message.type){
                case SET_RENDER_RESOLUTION:
                    ClientSurface.instances.setRenderResolution(new Vec3d(message.width, message.height, message.scale), message.instanceUUID);
                    break;
                case ASYNC_SCREEN_SIZES:
                    //ClientSurface.instances.sendResolution();
                    break;
                case SYNC_SCREEN_SIZE:
                    //return new ClientEventPacket(ben_mkiv.rendertoolkit.network.EventType.GLASSES_SCREEN_SIZE, ClientSurface.instances.getRenderOffset());
            }

            return null;
        }
    }

}