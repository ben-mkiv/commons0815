package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.component.wavefrontObj.Face;
import ben_mkiv.rendertoolkit.common.widgets.component.wavefrontObj.PolyLine;
import ben_mkiv.rendertoolkit.common.widgets.component.wavefrontObj.Vertex;
import ben_mkiv.rendertoolkit.common.widgets.component.wavefrontObj.objParser;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IOBJModel;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public abstract class OBJModelOC extends WidgetGLWorld implements IOBJModel {
    public Tessellator TESR;
    public BufferBuilder buffer;

    private String objData = "";

    public objParser objFile = null;

    public void loadOBJ(String objData){
        this.objData = objData;
        this.objFile = objData.length() > 0 ? new objParser(objData) : null;
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        buff.writeBoolean(!objData.equals("none"));
        if(!objData.equals("none")){
            byte[] utf8Bytes = this.objData.getBytes(Charsets.UTF_8);
            buff.writeInt(utf8Bytes.length);
            buff.writeBytes(utf8Bytes);
        }
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        if(buff.readBoolean())
            loadOBJ(buff.readBytes(buff.readInt()).toString(Charsets.UTF_8));
        else
            loadOBJ("");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableOBJModel();
    }

    @SideOnly(Side.CLIENT)
    public class RenderableOBJModel extends RenderableGLWidget {
        public VertexFormat malisisVertexFormat = new VertexFormat() {
            {
                addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
                addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
                addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
                addElement(new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2));
                addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
                addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
            }
        };

        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(objFile == null) return;

            int color = this.preRender(conditionStates);
            this.applyModifiers(conditionStates);

            TESR = Tessellator.getInstance();
            buffer = TESR.getBuffer();

            if(rendertype == RenderType.WorldLocated) {
                GlStateManager.rotate(180, 0, 0, 1);
                this.addPlayerRotation(player);
                GlStateManager.rotate(180, 0, 0, 1);
            }

            if(objFile.facesTri.size() > 0) {
                buffer.begin(GL11.GL_TRIANGLES, malisisVertexFormat);
                renderList(objFile.facesTri, color);
                TESR.draw();
            }

            if(objFile.facesQuad.size() > 0) {
                buffer.begin(GL11.GL_QUADS, malisisVertexFormat);
                renderList(objFile.facesQuad, color);
                TESR.draw();
            }

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int a = (color >> 24) & 0xff;

            if (objFile.lines.size() > 0) {
                buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
                readPolylineList(objFile.lines, r, g, b, a);
                TESR.draw();
            }

            this.postRender();
        }

        public void renderList(List<Face> faces, int color) {
            for (Face f : faces)
                for (int i = 0; i < f.getVertexes().length; i++) {
                    buffer.addVertexData(f.getVertexes()[i].setColor((color)).setAlpha((color >> 24)).getVertexData(malisisVertexFormat, null));

                }
        }
        public void readPolylineList(List<PolyLine> lines, int r, int g, int b, int a) {
            for (PolyLine line : lines) {
                Vertex[] vertexes = line.getVertexes();
                for (int i = 0; i < vertexes.length - 1; i++) {
                    Vertex v1 = vertexes[i];
                    Vertex v2 = vertexes[i + 1];
                    buffer.pos(v1.getX(), v1.getY(), v1.getZ()).color(r, g, b, a).endVertex();
                    buffer.pos(v2.getX(), v2.getY(), v2.getZ()).color(r, g, b, a).endVertex();
                }
            }
        }

    }

}
