package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.network.EventType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientEventPacket implements IMessage {
    protected int pentid, dimId, targetId = -1;
    protected BlockPos pos = new BlockPos(0, 0, 0);
    protected EnumFacing face = EnumFacing.DOWN;

    int x,y,mb;

    public ClientEventPacket() {}

    public ClientEventPacket(EventType type, PlayerInteractEvent event){
        EntityPlayer player = event.getEntityPlayer();

        this.pentid = player.getEntityId();
        this.dimId = player.dimension;
        this.pos = event.getPos();
        this.face = event.getFace();


    }

    public ClientEventPacket(EventType type, EntityPlayer player, Vec3d mousePos, int mouseButton) {
        this.pentid = player.getEntityId();
        this.dimId = player.dimension;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pentid = buf.readInt();
        this.dimId = buf.readInt();
        this.targetId = buf.readInt();

        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        this.face = EnumFacing.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pentid);
        buf.writeInt(this.dimId);
        buf.writeInt(this.targetId);

        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());

        buf.writeInt(this.face.ordinal());
    }

    public static class Handler implements IMessageHandler<ClientEventPacket, IMessage> {

        @Override
        public IMessage onMessage(ClientEventPacket message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.dimId);

            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.pentid);

            if (player == null)
                return null;



            /*
            ItemStack stackUsed = player.getHeldItemMainhand();
            Entity target = null;

            if(message.targetId != -1) {
                target = world.getEntityByID(message.targetId);

                if(target == null)
                    return null;
            }
            */


            return null;
        }
    }

}