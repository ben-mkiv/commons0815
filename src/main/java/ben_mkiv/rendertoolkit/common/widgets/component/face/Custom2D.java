package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.CustomShape;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class Custom2D extends CustomShape implements IAutoTranslateable {
    //for real this is needed!

    public Custom2D(){
        super();
        this.rendertype = RenderType.GameOverlayLocated;
    }

    @Override
    public WidgetType getType() {
        return WidgetType.CUSTOM2D;
    }
}
