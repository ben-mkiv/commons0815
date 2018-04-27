package ben_mkiv.guitoolkit.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EnergyBar extends prettyButton {
    public EnergyBar(int id, int x, int y, int width, int height){
        super(id, x, y, width, height, "");
        enabled = false;
    }

    public void drawBar(int offsetX, int offsetY, double percent, ResourceLocation bar){
        Tessellator tes = Tessellator.getInstance();

        if(bar != null)
            Minecraft.getMinecraft().renderEngine.bindTexture(bar);

        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        percent = (percent > 1) ? 1 : percent;

        double dw = this.width * percent;

        GL11.glColor4f(0.9F, 0.2F, 0.2F, 0.3F);
        int x = this.x + offsetX;
        int y = this.y + offsetY;

        BufferBuilder vertexbuffer = tes.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x+ width, y + height, 1).tex(1, 1).endVertex();
        vertexbuffer.pos(x+ width, y, 1).tex(1, 0).endVertex();
        vertexbuffer.pos(x+ dw, y, 1).tex(0, 0).endVertex();
        vertexbuffer.pos(x+ dw, y + height, 1).tex(0, 1).endVertex();
        tes.draw();

        GL11.glColor4f(0, 0.8F, 0, 0.5F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x + dw, y + height, 1).tex(1, 1).endVertex();
        vertexbuffer.pos(x + dw, y, 1).tex(1, 0).endVertex();
        vertexbuffer.pos(x, y, 1).tex(0, 0).endVertex();
        vertexbuffer.pos(x, y + height, 1).tex(0, 1).endVertex();
        tes.draw();

        RenderHelper.enableStandardItemLighting();
        GL11.glDisable(GL11.GL_BLEND);
    }
}