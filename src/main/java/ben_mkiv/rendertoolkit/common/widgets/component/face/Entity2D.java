package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.EntityWidget;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAlignable;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class Entity2D extends EntityWidget
		implements IAutoTranslateable, IAlignable {

	public Entity2D(){
		super();
		this.rendertype = RenderType.GameOverlayLocated;
	}
}

