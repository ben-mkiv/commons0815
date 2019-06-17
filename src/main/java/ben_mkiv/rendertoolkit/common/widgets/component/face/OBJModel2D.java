package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.OBJModelOC;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class OBJModel2D extends OBJModelOC implements IAutoTranslateable {
    public OBJModel2D(){
        super();
        this.rendertype = RenderType.GameOverlayLocated;
    }


    //for real this is needed!
    @Override
    public WidgetType getType() {
        return WidgetType.OBJMODEL2D;
    }
}
