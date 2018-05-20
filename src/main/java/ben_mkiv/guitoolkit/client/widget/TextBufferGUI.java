package ben_mkiv.guitoolkit.client.widget;

import ben_mkiv.rendertoolkit.common.widgets.component.face.Box2D;
import li.cil.oc.api.internal.TextBuffer;
import li.cil.oc.client.renderer.TextBufferRenderCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class TextBufferGUI {
    public abstract TextBuffer get(); // //return oeGUI.entity.getCapability(OC_ENTITY, null).getScreen();

    static Box2D background = new Box2D();
    public final int padding = 2;

    public TextBufferGUI(){
        background.WidgetModifierList.addColor(0, 0, 0, 1);
        background.WidgetModifierList.addColor(0, 0, 0, 1);

        if(this.get() == null)
            return;
    }

    public float scaleToWidth = 1, renderWidth, renderHeight;

    public void render(int x, int y, int width, double scale) {
        if(this.get() == null)
            return;

        renderWidth = getRenderWidth(Integer.MAX_VALUE);
        renderHeight = getRenderHeight(Math.round(renderWidth));
        background.setSize(width + 2*padding, renderHeight + 2*padding);
        scaleToWidth = getRenderScale(width);

        //Minecraft.getMinecraft().entityRenderer.disableLightmap();
        //RenderHelper.disableStandardItemLighting();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();
            GL11.glTranslated(x, y, 0);

            //dont scale for variations which are only slightly off scale
            if(Math.abs(scaleToWidth) > 1.01)
                GL11.glScalef(scaleToWidth, scaleToWidth, 1);

            background.getRenderable().render(Minecraft.getMinecraft().player, new Vec3d(0, 0, 0), 0);

            GL11.glColor4f(1, 1, 1, 1);
            GL11.glTranslated(padding, padding, 0);
            this.get().renderText();
        GL11.glPopMatrix();


        RenderHelper.enableStandardItemLighting();
        GL11.glDisable(GL11.GL_BLEND);
    }

    float getRenderScale(float width){
        return (width / renderWidth);
    }

    public int getRenderWidth(int maxWidth){
        return Math.min(maxWidth, TextBufferRenderCache.renderer().charRenderWidth() * this.get().getWidth());
    }

    public int getRenderHeight(int maxWidth){
        return Math.round(getRenderScale(maxWidth) * TextBufferRenderCache.renderer().charRenderHeight() * this.get().getHeight());
    }
}
