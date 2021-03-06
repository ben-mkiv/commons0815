package ben_mkiv.rendertoolkit.common.widgets;

import ben_mkiv.rendertoolkit.common.widgets.component.face.*;
import ben_mkiv.rendertoolkit.common.widgets.component.world.*;

public enum WidgetType {
	CUBE3D(Cube3D.class),
	BOX2D(Box2D.class),
	TEXT2D(Text2D.class),
	TEXT3D(Text3D.class),
	CUSTOM2D(Custom2D.class),
	CUSTOM3D(Custom3D.class),
	ITEM2D(Item2D.class),
	ITEM3D(Item3D.class),
	OBJMODEL2D(OBJModel2D.class),
	OBJMODEL3D(OBJModel3D.class),
	ENTITY2D(Entity2D.class),
	FLUID2D(Fluid2D.class),
	ENTITYTRACKER3D(EntityTracker3D.class),
	OCSCREEN2D(ocScreen2D.class),
	OCSCREEN3D(ocScreen3D.class);

	Class<? extends Widget> clazz;

	WidgetType(Class<? extends Widget> cl) {
		clazz = cl;
	}

}
