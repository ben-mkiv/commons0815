package ben_mkiv.rendertoolkit.common.widgets.component.world;


import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.ItemIcon;

public class Item3D extends ItemIcon {
	public Item3D(){}

	@Override
	public WidgetType getType() {
		return WidgetType.ITEM3D;
	}
}

