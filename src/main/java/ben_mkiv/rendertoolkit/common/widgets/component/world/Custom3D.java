package ben_mkiv.rendertoolkit.common.widgets.component.world;

import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.CustomShape;

public class Custom3D extends CustomShape {
    //for real this is needed!
    @Override
    public WidgetType getType() {
        return WidgetType.CUSTOM3D;
    }
}
