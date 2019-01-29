package ben_mkiv.rendertoolkit.network.messages;

import ben_mkiv.rendertoolkit.common.widgets.Widget;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetUpdatePacket  implements IMessage {
    public enum Action { AddWigets, RemoveWidgets, RemoveAllWidgets }

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


    public WidgetUpdatePacket(int id, @Nonnull Widget widget) {
        this.widgetList = new HashMap<>();
        widgetList.put(id, widget);
        this.type = Action.AddWigets;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Action.values()[buf.readInt()];

        switch (type) {
            case AddWigets:
                widgetList = new HashMap<>();
                for(int size = buf.readInt(), i = 0; i < size; i++){
                    Widget w = Widget.create(buf.readInt());
                    w.read(buf);
                    widgetList.put(buf.readInt(), w);
                }
                break;
            case RemoveWidgets:
                ids = new ArrayList<>();
                for(int size = buf.readInt(), i = 0; i < size; i++)
                    ids.add(buf.readInt());
                break;
            case RemoveAllWidgets:
            default:
                break;

        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type.ordinal());
        switch (type) {
            case AddWigets:
                buf.writeInt(widgetList.size());
                for(Map.Entry<Integer, Widget> w : widgetList.entrySet()){
                    buf.writeInt(w.getValue().getType().ordinal());
                    w.getValue().write(buf);
                    buf.writeInt(w.getKey());
                }
                break;
            case RemoveWidgets:
                buf.writeInt(ids.size());
                for(Integer id : ids)
                    buf.writeInt(id);
                break;
            case RemoveAllWidgets:
            default:
                break;

        }
    }

    public static class Handler implements IMessageHandler<WidgetUpdatePacket, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(WidgetUpdatePacket message, MessageContext ctx) {

            switch (message.type) {
                case AddWigets:         ClientSurface.instances.updateWidgets(message.widgetList.entrySet()); break;
                case RemoveWidgets:     ClientSurface.instances.removeWidgets(message.ids); break;
                case RemoveAllWidgets:  ClientSurface.instances.removeAllWidgets(); break;
            }
            return null;
        }
    }

}