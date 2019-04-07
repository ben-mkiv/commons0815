package ben_mkiv.rendertoolkit.common.widgets.component.world;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLOverlay;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.component.common.TextWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class Text3D extends TextWidget {
	public Text3D() {
		super();
		faceWidgetToPlayer = true;
		valign = WidgetGLOverlay.VAlignment.MIDDLE;
		halign = WidgetGLOverlay.HAlignment.CENTER;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.TEXT3D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderableFloatingText();
	}

	@SideOnly(Side.CLIENT)
	class RenderableFloatingText extends WidgetGLOverlay.RenderableGLWidget {
		final float scale = 0.1F;


		@Override
		public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
			if(getText().length() < 1) return;

			updateStringDimensions();
			updateAlignments();
			int currentColor = this.preRender(conditionStates);

			// center text on current block position
			GlStateManager.translate(0.5F, 0.5F, 0.5F);
			this.applyModifiers(conditionStates);


			if(faceWidgetToPlayer) {
				GL11.glRotated(-player.rotationYaw, 0.0D, 1.0D, 0.0D);
				GL11.glRotated(player.rotationPitch, 1.0D, 0.0D, 0.0D);
			}

			GlStateManager.scale(scale, scale, scale);

			GlStateManager.translate(-offsetX, -offsetY, 0.0D);

			GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
			drawString(currentColor);

			this.postRender();
		}
	}


}
