package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.ItemIcon;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAlignable;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;

public class Item2D extends ItemIcon implements IAutoTranslateable, IAlignable {
	public Item2D(){
		this.rendertype = RenderType.GameOverlayLocated;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.ITEM2D;
	}

}

