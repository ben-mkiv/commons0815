package ben_mkiv.rendertoolkit.common.widgets.component.face;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.TextWidget;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IAutoTranslateable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Text2D extends TextWidget implements IAutoTranslateable {

	public Text2D() {
		super();
		this.rendertype = RenderType.GameOverlayLocated;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.TEXT2D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderText();
	}
	
	class RenderText extends RenderableGLWidget{
		@Override
		public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
			if(getText().length() < 1) return;

			updateStringDimensions();
			updateAlignments();

			int currentColor = this.preRender(conditionStates);
			this.applyModifiers(conditionStates);

			GlStateManager.translate(offsetX, offsetY, 0F);

			drawString(currentColor);

			this.postRender();
		}


	}
}
