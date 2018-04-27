package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.ItemIcon;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAlignable;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class Item2D extends ItemIcon implements IAutoTranslateable, IAlignable {
	public Item2D(){
		super();
		this.rendertype = RenderType.GameOverlayLocated;
	}


}

