package ben_mkiv.rendertoolkit.common.widgets.component.world;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.ocScreenWidget;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ocScreen3D extends ocScreenWidget {

    @Override
    public WidgetType getType() {
        return WidgetType.OCSCREEN3D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableOCScreen3D();
    }

    private class RenderableOCScreen3D extends RenderableOCScreen {
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(screen == null)
                return;

            if(renderToolkit.Albedo || renderToolkit.Optifine)
                GlStateManager.disableLighting();

            //GlStateManager.translate(screen.getPos().getX(), screen.getPos().getY(), screen.getPos().getZ());

            //rotateByOrigin(screen.yaw(), screen.pitch());

            super.render(conditionStates, true);

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

        void rotateByOrigin(EnumFacing yaw, EnumFacing pitch){
            GlStateManager.translate(0.5, 0.5, 0.5);

            switch(yaw){
                case WEST: GlStateManager.rotate(-90, 0, 1, 0); break;
                case NORTH: GlStateManager.rotate(180, 0, 1, 0); break;
                case EAST: GlStateManager.rotate(90, 0, 1, 0); break;
            }

            switch(pitch){
                case DOWN: GlStateManager.rotate(90, 1, 0, 0); break;
                case UP: GlStateManager.rotate(-90, 1, 0, 0); break;
            }

            GlStateManager.translate(-0.5, -0.5, -0.5);
        }


    }


}
