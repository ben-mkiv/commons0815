package ben_mkiv.rendertoolkit.surface;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WidgetCollection {

    public Map<Integer, IRenderableWidget> renderables = new ConcurrentHashMap();
    public Map<Integer, IRenderableWidget> renderablesWorld = new ConcurrentHashMap();


    public Map<Integer, IRenderableWidget> getWidgetsWorld(){
        return renderablesWorld;
    }

    public Map<Integer, IRenderableWidget>getWidgetsOverlay(){
        return renderables;
    }

    //gets the current widgets and puts them to the correct hashmap
    public void updateWidgets(Set<Map.Entry<Integer, Widget>> widgets){
        for(Map.Entry<Integer, Widget> widget : widgets){
            IRenderableWidget r = widget.getValue().getRenderable();
            switch(r.getRenderType()){
                case GameOverlayLocated:
                    renderables.put(widget.getKey(), r);
                    break;
                case WorldLocated:
                    renderablesWorld.put(widget.getKey(), r);
                    break;
            }
        }
    }

    public void removeWidgets(List<Integer> ids){
        for(Integer id : ids){
            renderables.remove(id);
            renderablesWorld.remove(id);
        }
    }

    public void removeAllWidgets(){
        renderables.clear();
        renderablesWorld.clear();
    }

    public int getWidgetCount(RenderType renderEvent) {
        if(renderEvent == null)
            return getWidgetsOverlay().size() + getWidgetsWorld().size();

        switch(renderEvent){
            case WorldLocated:
                return getWidgetsWorld().size();
            case GameOverlayLocated:
                return getWidgetsOverlay().size();
        }

        return 0;
    }

}
