package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.FluidWidget;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAlignable;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class Fluid2D extends FluidWidget implements IAutoTranslateable, IAlignable {
	public Fluid2D(){
		super();
		this.rendertype = RenderType.GameOverlayLocated;
	}


}

