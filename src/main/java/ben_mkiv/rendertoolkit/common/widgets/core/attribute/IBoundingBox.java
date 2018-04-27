package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

import net.minecraft.util.math.AxisAlignedBB;

public interface IBoundingBox extends IAttribute{
    void setSize3D(int x1, int y1, int z1, int x2, int y2, int z2);
    AxisAlignedBB getBoundingBox();
}