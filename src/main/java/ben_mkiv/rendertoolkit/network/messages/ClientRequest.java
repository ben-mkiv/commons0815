package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.renderToolkit;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.logging.Logger;

public class ClientRequest implements IMessage {
    public enum EventType{
        SYNC_SCREEN_SIZE,
        ASYNC_SCREEN_SIZES,
        SET_RENDER_RESOLUTION
    }

    protected int pentid, dimId, targetId = -1;

    public ClientRequest() {}

    //todo: fix openglasses screen size network packets
    public ClientRequest(EventType type) {
        Logger.getLogger(renderToolkit.MODID).warning("THIS IS NUTZ");
    }

    public ClientRequest(EventType type, EntityPlayer player, Vec3d mousePos, int mouseButton) {
        this.pentid = player.getEntityId();
        this.dimId = player.dimension;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pentid = buf.readInt();
        this.dimId = buf.readInt();
        this.targetId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pentid);
        buf.writeInt(this.dimId);
        buf.writeInt(this.targetId);
    }

    public static class Handler implements IMessageHandler<ClientRequest, IMessage> {

        @Override
        public IMessage onMessage(ClientRequest message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.dimId);

            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.pentid);


            if (player == null)
                return null;

            ItemStack stackUsed = player.getHeldItemMainhand();

            Entity target = null;

            if(message.targetId != -1) {
                target = world.getEntityByID(message.targetId);

                if(target == null)
                    return null;
            }


            return null;
        }
    }

}