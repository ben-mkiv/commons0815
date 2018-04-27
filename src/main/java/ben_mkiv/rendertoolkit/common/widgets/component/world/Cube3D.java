package ben_mkiv.rendertoolkit.common.widgets.component.world;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;

public class Cube3D extends WidgetGLWorld {
	public Cube3D() { super(); }

	@SideOnly(Side.CLIENT)
	@Override
	public IRenderableWidget getRenderable() {
		return new RenderCube3D();
	}
	
	@SideOnly(Side.CLIENT)
	class RenderCube3D extends RenderableGLWidget{
		@Override
		public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
			this.preRender(conditionStates, renderOffset);

			this.applyModifiers(conditionStates);
						
			GL11.glBegin(GL11.GL_QUADS);    // Draw The Cube Using quads			    
			GL11.glVertex3f(1.0f,1.0f,0.0f);    // Top Right Of The Quad (Top)
			GL11.glVertex3f(0.0f,1.0f,0.0f);    // Top Left Of The Quad (Top)
			GL11.glVertex3f(0.0f,1.0f,1.0f);    // Bottom Left Of The Quad (Top)
			GL11.glVertex3f(1.0f,1.0f,1.0f);    // Bottom Right Of The Quad (Top)

			GL11.glVertex3f(1.0f,0.0f,1.0f);    // Top Right Of The Quad (Bottom)
			GL11.glVertex3f(0.0f,0.0f,1.0f);    // Top Left Of The Quad (Bottom)
			GL11.glVertex3f(0.0f,0.0f,0.0f);    // Bottom Left Of The Quad (Bottom)
			GL11.glVertex3f(1.0f,0.0f,0.0f);    // Bottom Right Of The Quad (Bottom)
  
			GL11.glVertex3f(1.0f,1.0f,1.0f);    // Top Right Of The Quad (Front)
			GL11.glVertex3f(0.0f,1.0f,1.0f);    // Top Left Of The Quad (Front)
			GL11.glVertex3f(0.0f,0.0f,1.0f);    // Bottom Left Of The Quad (Front)
			GL11.glVertex3f(1.0f,0.0f,1.0f);    // Bottom Right Of The Quad (Front)

			GL11.glVertex3f(1.0f,0.0f,0.0f);    // Top Right Of The Quad (Back)
			GL11.glVertex3f(0.0f,0.0f,0.0f);    // Top Left Of The Quad (Back)
			GL11.glVertex3f(0.0f,1.0f,0.0f);    // Bottom Left Of The Quad (Back)
			GL11.glVertex3f(1.0f,1.0f,0.0f);    // Bottom Right Of The Quad (Back)

			GL11.glVertex3f(0.0f,1.0f,1.0f);    // Top Right Of The Quad (Left)
			GL11.glVertex3f(0.0f,1.0f,0.0f);    // Top Left Of The Quad (Left)
			GL11.glVertex3f(0.0f,0.0f,0.0f);    // Bottom Left Of The Quad (Left)
			GL11.glVertex3f(0.0f,0.0f,1.0f);    // Bottom Right Of The Quad (Left)

			GL11.glVertex3f(1.0f,1.0f,0.0f);    // Top Right Of The Quad (Right)
			GL11.glVertex3f(1.0f,1.0f,1.0f);    // Top Left Of The Quad (Right)
			GL11.glVertex3f(1.0f,0.0f,1.0f);    // Bottom Left Of The Quad (Right)
			GL11.glVertex3f(1.0f,0.0f,0.0f);    // Bottom Right Of The Quad (Right)		
			GL11.glEnd();            // End Drawing The Cube
						
		    this.postRender();
		}
	}
}
