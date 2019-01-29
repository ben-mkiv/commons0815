package ben_mkiv.rendertoolkit.common.widgets.component.world;

import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.OBJModelOC;

public class OBJModel3D extends OBJModelOC {
    //for real this is needed!
    @Override
    public WidgetType getType() {
        return WidgetType.OBJMODEL3D;
    }
}
