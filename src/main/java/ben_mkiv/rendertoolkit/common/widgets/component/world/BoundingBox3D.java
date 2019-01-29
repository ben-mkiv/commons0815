package ben_mkiv.rendertoolkit.common.widgets.component.world;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IBoundingBox;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class BoundingBox3D extends Cube3D implements IBoundingBox {
    public BoundingBox3D() {
        setSize3D(0, 0, 0, 1, 1, 1);
    }

    AxisAlignedBB box;
    ArrayList<Cube3D> cubes = new ArrayList();

    @Override
    public void setSize3D(int x1, int y1, int z1, int x2, int y2, int z2){
        this.box = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
        cubes.clear();
        for(double x = box.minX; x < box.maxX; x++)
            for(double y = box.minY; y < box.maxY; y++)
                for(double z = box.minZ; z < box.maxZ; z++) {
                    Cube3D cube = new Cube3D();
                    cube.WidgetModifierList.addTranslate((float) x, (float) y, (float) z);
                    cube.WidgetModifierList.addColor(1F, 1F, 1F, 0.2F);
                    cube.WidgetModifierList.addScale(0.7F, 0.7F, 0.7F);
                    cubes.add(cube);
                }
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);
        buff.writeDouble(box.minX);
        buff.writeDouble(box.minY);
        buff.writeDouble(box.minZ);
        buff.writeDouble(box.maxX);
        buff.writeDouble(box.maxY);
        buff.writeDouble(box.maxZ);
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);
        setSize3D((int) buff.readDouble(), (int) buff.readDouble(), (int) buff.readDouble(), (int) buff.readDouble(), (int) buff.readDouble(), (int) buff.readDouble());
    }

    @Override
    public AxisAlignedBB getBoundingBox(){ return this.box; }


    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new BoundingBox3D.RenderBoundingBox3D();
    }

    @SideOnly(Side.CLIENT)
    class RenderBoundingBox3D extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            //GlStateManager.disableCull();

            for(Cube3D cube : cubes)
                cube.getRenderable().render(player, renderOffset, conditionStates);
        }

        public void renderCube(EntityPlayer player, Vec3d renderOffset, long conditionStates){
            this.preRender(conditionStates);

            this.applyModifiers(conditionStates);

            GlStateManager.disableCull();

            GL11.glBegin(GL11.GL_QUADS);    // Draw The Cube Using quads
            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);    // Top Right Of The Quad (Top)
            GL11.glVertex3d(box.minX, box.maxY, box.minZ);    // Top Left Of The Quad (Top)
            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);    // Bottom Left Of The Quad (Top)
            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);    // Bottom Right Of The Quad (Top)


            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);    // Top Right Of The Quad (Bottom)
            GL11.glVertex3d(box.minX, box.minY, box.maxZ);    // Top Left Of The Quad (Bottom)
            GL11.glVertex3d(box.minX, box.minY, box.minZ);    // Bottom Left Of The Quad (Bottom)
            GL11.glVertex3d(box.maxX, box.minY, box.minZ);    // Bottom Right Of The Quad (Bottom)

            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);    // Top Right Of The Quad (Front)
            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);    // Top Left Of The Quad (Front)
            GL11.glVertex3d(box.minX, box.minY, box.maxZ);    // Bottom Left Of The Quad (Front)
            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);    // Bottom Right Of The Quad (Front)

            GL11.glVertex3d(box.maxX, box.minY, box.minZ);    // Top Right Of The Quad (Back)
            GL11.glVertex3d(box.minX, box.minY, box.minZ);    // Top Left Of The Quad (Back)
            GL11.glVertex3d(box.minX, box.maxY, box.minZ);    // Bottom Left Of The Quad (Back)
            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);    // Bottom Right Of The Quad (Back)

            GL11.glVertex3d(box.minX, box.maxY, box.maxZ);    // Top Right Of The Quad (Left)
            GL11.glVertex3d(box.minX, box.maxY, box.minZ);    // Top Left Of The Quad (Left)
            GL11.glVertex3d(box.minX, box.minY, box.minZ);    // Bottom Left Of The Quad (Left)
            GL11.glVertex3d(box.minX, box.minY, box.maxZ);    // Bottom Right Of The Quad (Left)

            GL11.glVertex3d(box.maxX, box.maxY, box.minZ);    // Top Right Of The Quad (Right)
            GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);    // Top Left Of The Quad (Right)
            GL11.glVertex3d(box.maxX, box.minY, box.maxZ);    // Bottom Left Of The Quad (Right)
            GL11.glVertex3d(box.maxX, box.minY, box.minZ);    // Bottom Right Of The Quad (Right)
            GL11.glEnd();            // End Drawing The Cube

            this.postRender();
        }
    }
}