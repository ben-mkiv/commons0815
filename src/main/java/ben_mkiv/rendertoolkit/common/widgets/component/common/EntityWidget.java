package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEntity;
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

            int alphaColor = this.preRender(conditionStates, renderOffset);
            this.applyModifiers(conditionStates);

            if(rendertype == RenderType.WorldLocated) {
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                GL11.glRotated(180D, 0, 1D, 0D);
                if(faceWidgetToPlayer) {
                    GL11.glRotated(player.rotationYaw, 0.0D, -1.0D, 0.0D);
                    GL11.glRotated(180D, 0, 1D, 0D);
                    GL11.glRotated(player.rotationPitch, 1.0D, 0.0D, 0.0D);
                    GL11.glRotated(180D, 0, 1D, 0D);
                }
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            }
            else {
                this.applyAlignments();
                GL11.glTranslatef(0F, 1F, 0F);
                GL11.glRotated(180, 1, 0, 0);
            }

            renderEntity();

            this.postRender();
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

        public void renderEntity() {
            if(entity == null || !(entity instanceof EntityLivingBase))
                return;
            
            float posX = 200, posY = 200;
            float scale = 40;
            int mouseX = 0;
            int mouseY = 0;

            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            GlStateManager.translate((float)posX, (float)posY, 50.0F);
            GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            float f = entity.renderYawOffset;
            float f1 = entity.rotationYaw;
            float f2 = entity.rotationPitch;
            float f3 = entity.prevRotationYawHead;
            float f4 = entity.rotationYawHead;
            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
            entity.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
            entity.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
            entity.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
            entity.rotationYawHead = entity.rotationYaw;
            entity.prevRotationYawHead = entity.rotationYaw;
            GlStateManager.translate(0.0F, 0.0F, 0.0F);
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setPlayerViewY(180.0F);
            rendermanager.setRenderShadow(false);

            rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);

            rendermanager.setRenderShadow(true);
            entity.renderYawOffset = f;
            entity.rotationYaw = f1;
            entity.rotationPitch = f2;
            entity.prevRotationYawHead = f3;
            entity.rotationYawHead = f4;
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }

    }

}


