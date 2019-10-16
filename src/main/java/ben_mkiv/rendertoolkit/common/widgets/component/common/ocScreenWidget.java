package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IScreen;
import li.cil.oc.api.internal.TextBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;

public abstract class ocScreenWidget extends WidgetGLWorld implements IScreen {
    public interface IScreenBlock{
        EnumFacing yaw();
        EnumFacing pitch();
        TextBuffer buffer();
        World getWorld();
        BlockPos getPos();
    }

    protected IScreenBlock screen;

    @Override
    public void bind(IScreenBlock tile){
        screen = tile;
    }

    public abstract class RenderableOCScreen extends RenderableGLWidget {
        public void render(long conditionStates, boolean renderBackside) {
            this.preRender(conditionStates);
            Color col = new Color(this.applyModifiers(conditionStates));

            GlStateManager.pushAttrib();
            GlStateManager.enableTexture2D();
            screen.buffer().renderText();

            if(renderBackside && col.getAlpha() > 0 && col.getAlpha() < 255){
                // render the whole text again on the backside as the oc method disables the depth mask
                GlStateManager.translate(0, 0, .0002);
                GlStateManager.disableCull();
                screen.buffer().renderText();
                GlStateManager.enableCull();
            }

            this.postRender();
            GlStateManager.popAttrib();
            GlStateManager.enableTexture2D();
        }

    }

}


