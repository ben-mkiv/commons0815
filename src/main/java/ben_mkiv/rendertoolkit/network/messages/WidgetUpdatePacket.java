package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.renderToolkit;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetUpdatePacket  implements IMessage {
    public enum Action{
        AddWigets,RemoveWidgets,RemoveAllWidgets;
    }

    public HashMap<Integer, Widget> widgetList;
    List<Integer> ids;
    Action type;

    public WidgetUpdatePacket() {
        type = Action.RemoveAllWidgets;
    }

    public WidgetUpdatePacket(HashMap<Integer, Widget> widgetList) {
        this.widgetList = widgetList;
        type = Action.AddWigets;
    }

    public WidgetUpdatePacket(List<Integer> l){
        ids = l;
        type = Action.RemoveWidgets;
    }

    public WidgetUpdatePacket(int id){
        ids = new ArrayList<Integer>();
        ids.add(id);
        type = Action.RemoveWidgets;
    }

    public WidgetUpdatePacket(int id, Widget widget) {
        this.widgetList = new HashMap<Integer,Widget>();
        widgetList.put(id, widget);
        this.type = Action.AddWigets;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Action.values()[buf.readInt()];
        switch (type) {
            case AddWigets: readOnAddAction(buf);
                break;
            case RemoveWidgets: readOnRemoveAction(buf);
                break;
            case RemoveAllWidgets: ;
                break;
            default:
                break;

        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type.ordinal());
        switch (type) {
            case AddWigets: writeOnAddAction(buf);
                break;
            case RemoveWidgets: writeOnRemoveAction(buf);
                break;
            case RemoveAllWidgets: ;
                break;
            default:
                break;

        }
    }

    private void readOnAddAction(ByteBuf buf) {
        widgetList = new HashMap<Integer,Widget>();
        int size = buf.readInt();
        for(int i=0; i<size ;i++){
            Widget w = Widget.create(buf.readInt());
            w.read(buf);
            widgetList.put(buf.readInt(), w);
        }
    }

    private void readOnRemoveAction(ByteBuf buf){
        ids = new ArrayList<Integer>();
        int size = buf.readInt();
        for(int i = 0; i<size; i++){
            ids.add(buf.readInt());
        }
    }

    private void writeOnAddAction(ByteBuf buf) {
        buf.writeInt(widgetList.size());
        for(Map.Entry<Integer, Widget> w : widgetList.entrySet()){
            buf.writeInt(w.getValue().getType());
            w.getValue().write(buf);
            buf.writeInt(w.getKey());
        }
    }

    private void writeOnRemoveAction(ByteBuf buf) {
        buf.writeInt(ids.size());
        for(Integer i: ids){
            buf.writeInt(i);
        }
    }


    public static class Handler implements IMessageHandler<WidgetUpdatePacket, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(WidgetUpdatePacket message, MessageContext ctx) {

            switch (message.type) {
                case AddWigets:
                    renderToolkit.ClientSurface.instances.updateWidgets(message.widgetList.entrySet());
                    break;
            }


            return null;
        }
    }

}