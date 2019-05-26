package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.ocScreenWidget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ocScreen2D extends ocScreenWidget {
    public ocScreen2D(){
        super();
        this.rendertype = RenderType.GameOverlayLocated;
    }

    //for real this is needed!
    @Override
    public WidgetType getType() {
        return WidgetType.OCSCREEN2D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableOCScreen2D();
    }

    private class RenderableOCScreen2D extends RenderableOCScreen {
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
           super.render(conditionStates, false);
        }

    }
}
