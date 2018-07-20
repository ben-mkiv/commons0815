package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IFluid {
    void setFluid(FluidStack newFluid);
    Fluid getFluid();
}
