package ben_mkiv.rendertoolkit.common.widgets.component.face;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class OBJModel2D extends ben_mkiv.rendertoolkit.common.widgets.component.common.OBJModelOC implements IAutoTranslateable  {
    public OBJModel2D(){
        super();
        this.rendertype = RenderType.GameOverlayLocated;
    }

}
