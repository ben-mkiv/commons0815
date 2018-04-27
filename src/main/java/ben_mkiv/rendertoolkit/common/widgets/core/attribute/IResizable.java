package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

public interface IResizable extends IAttribute{
	void setSize(double width, double height);
	double getWidth();
	double getHeight();
}
