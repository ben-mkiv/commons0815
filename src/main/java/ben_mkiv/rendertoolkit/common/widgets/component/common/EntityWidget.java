package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEntity;
import ben_mkiv.rendertoolkit.common.widgets.core.modifiers.WidgetModifierRotate;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public abstract class EntityWidget extends WidgetGLWorld implements IEntity {
    EntityLivingBase entity = null;

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        if(entity != null) {
            ByteBufUtils.writeTag(buff, entity.serializeNBT());
        }
        else
            ByteBufUtils.writeTag(buff, new NBTTagCompound());
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        NBTTagCompound entityNBT = ByteBufUtils.readTag(buff);
        if(!entityNBT.equals(new NBTTagCompound()))
            setEntity(entityNBT, Minecraft.getMinecraft().player.world);
        else
            this.entity = null;
    }

    @Override
    public boolean setEntity(EntityLivingBase entity) {
        setEntity(entity.serializeNBT(), entity.world);
        return true;
    }

    @Override
    public boolean setEntity(NBTTagCompound entityNBT, World world) {
        this.entity = (EntityLivingBase) EntityList.createEntityFromNBT(entityNBT, world);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableEntity();
    }

    public class RenderableEntity extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(entity == null)
                return;

            preRender(conditionStates);
            applyModifiers(conditionStates);

            if(rendertype == RenderType.WorldLocated) {
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.rotate(180, 0, 1, 0);
                if(faceWidgetToPlayer) {
                    GlStateManager.rotate(player.rotationYaw, 0, -1, 0);
                    GlStateManager.rotate(180, 0, 1, 0);
                    GlStateManager.rotate(player.rotationPitch, 1, 0, 0);
                    GlStateManager.rotate(180, 0, 1, 0);
                }
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            }
            else {
                applyAlignments();
                GlStateManager.translate(0F, 1F, 0F);
                GlStateManager.rotate(180, 1, 0, 0);
            }

            renderEntity(((WidgetModifierRotate) WidgetModifierList.modifiers.get(0)).Y, renderOffset);

            postRender();
        }

        private void applyAlignments(){
            switch(this.getHorizontalAlign()) {
                case CENTER:
                    GL11.glTranslatef(-0.5F, 0F, 0F);
                    break;
                case RIGHT:
                    GL11.glTranslatef(-1F, 0F, 0F);
                    break;
            }

            switch(this.getVerticalAlign()) {
                case MIDDLE:
                    GL11.glTranslatef(0F, -0.5F, 0F);
                    break;
                case BOTTOM:
                    GL11.glTranslatef(0F, -1F, 0F);
                    break;
            }
        }

        void renderEntity(float rotation, Vec3d location) {
            if(entity == null)
                return;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();

            GlStateManager.translate(location.x, -location.y, -50F);
            GlStateManager.scale(50, 50, 50);

            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);

            //entity.rotationYawHead = entity.prevRotationYawHead = entity.rotationYaw = entity.renderYawOffset = -180 + rotation * 360;

            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderShadow(false);
            rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0, 1.0F, false);
            rendermanager.setRenderShadow(true);

            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }


    }

}
