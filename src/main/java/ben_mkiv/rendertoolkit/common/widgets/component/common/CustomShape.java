package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ICustomShape;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class CustomShape extends WidgetGLWorld implements ICustomShape {
    private HashMap<Integer, ArrayList> vectors, vectorsCache;
    private ArrayList<ArrayList> triangleCache = new ArrayList<>();
    private boolean updateCache = true;
    public boolean gl_strips;
    public boolean smooth_shading;

    public CustomShape() {
        vectors = new HashMap<>();
        gl_strips = false;
        smooth_shading = false;
    }

    public int getVertexCount(){
        return this.vectors.size();
    }

    private void syncCache(){
        if(!updateCache)
            return;

        vectorsCache = new HashMap<>(vectors);

        if(!gl_strips){
            triangleCache = new ArrayList<>();
            ArrayList<ArrayList> triangle = new ArrayList<>();

            for(ArrayList vector : vectorsCache.values()){
                if(triangle.size() == 3) {
                    triangleCache.add(triangle);
                    triangle = new ArrayList<>();
                }

                triangle.add(vector);
            }

        }

        updateCache = false;
    }

    public void setVertex(int n, float nx, float ny, float nz){
        if(vectors.containsKey(n)) {
            vectors.get(n).set(0, nx);
            vectors.get(n).set(1, ny);
            vectors.get(n).set(2, nz);

            updateCache = true;
        }
        else
            addVertex(nx, ny, nz);
    }

    public int addVertex(float nx, float ny, float nz){
        ArrayList<Float> vector = new ArrayList<>();
        vector.add(nx);
        vector.add(ny);
        vector.add(nz);
        int index = getVertexIndex();

        this.vectors.put(index, vector);

        updateCache = true;

        return index;
    }

    private int getVertexIndex(){
        int index = 0;
        for(int vertIndex : new HashSet<>(vectors.keySet()))
            if(vertIndex > index)
                index = vertIndex;

        return index + 1;
    }

    public void removeVertex(int n){
        if(vectors.containsKey(n)) {
            this.vectors.remove(n);
            updateCache = true;
        }
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        buff.writeBoolean(gl_strips);

        HashMap<Integer, ArrayList> vectorBuffer = new HashMap<>(vectors);

        buff.writeInt(vectors.size());

        for(Map.Entry<Integer, ArrayList> entry : vectorBuffer.entrySet()) {
            buff.writeInt(entry.getKey());
            buff.writeFloat((float) entry.getValue().get(0));
            buff.writeFloat((float) entry.getValue().get(1));
            buff.writeFloat((float) entry.getValue().get(2));
        }
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        this.gl_strips = buff.readBoolean();

        vectors = new HashMap<>();
        for(int i = 0, vectorCount = buff.readInt(); i < vectorCount; i++) {
            setVertex(buff.readInt(), buff.readFloat(), buff.readFloat(), buff.readFloat());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableCustom();
    }

    @SideOnly(Side.CLIENT)
    public class RenderableCustom extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            syncCache();

            if(vectorsCache.size() <3) return;

            this.preRender(conditionStates);
            this.applyModifiers(conditionStates);
            if(smooth_shading)
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            else
                GlStateManager.shadeModel(GL11.GL_FLAT);

            if(gl_strips) {
                GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);
                for(ArrayList vector : vectorsCache.values())
                    GlStateManager.glVertex3f((float) vector.get(0), (float) vector.get(1), (float) vector.get(2));
            }
            else{
                GL11.glBegin(GL11.GL_TRIANGLES);
                for(ArrayList<ArrayList> tri : triangleCache) {
                    for(ArrayList vector : tri)
                        GlStateManager.glVertex3f((float) vector.get(0), (float) vector.get(1), (float) vector.get(2));
                }
            }

            GlStateManager.glEnd();

            this.postRender();
        }
    }
}
