package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.surface.ClientSurface;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientRequest implements IMessage {
    public enum EventType{
        SYNC_SCREEN_SIZE,
        ASYNC_SCREEN_SIZES,
        SET_RENDER_RESOLUTION
    }

    protected double width,height,scale;

    protected EventType type;

    public ClientRequest() {}

    //SET_RENDER_RESOLUTION
    public ClientRequest(EventType type, double width, double height, double scale) {
        this(type);
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    //SYNC_SCREEN_SIZE, ASYNC_SCREEN_SIZES
    public ClientRequest(EventType type) {
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = EventType.values()[buf.readInt()];

        if(this.type.equals(EventType.SET_RENDER_RESOLUTION)){
            this.width = buf.readDouble();
            this.height = buf.readDouble();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
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
                    ClientSurface.instances.renderResolution = new Vec3d(message.width, message.height, message.scale);
                    break;
                case ASYNC_SCREEN_SIZES:

                    break;
                case SYNC_SCREEN_SIZE:
                    ClientSurface.instances.sendResolution();
                    break;
            }

            return null;
        }
    }

}