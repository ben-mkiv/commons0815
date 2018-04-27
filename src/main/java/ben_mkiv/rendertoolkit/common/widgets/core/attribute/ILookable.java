package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

public interface ILookable extends IAttribute{
	void setLookingAt(double x, double y, double z);
	boolean isLookingAtEnable();
	void setLookingAtEnable(boolean enable);
	double getLookingAtX();
	double getLookingAtY();
	double getLookingAtZ();
}
