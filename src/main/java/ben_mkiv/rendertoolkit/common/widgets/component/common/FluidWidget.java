package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IFluid;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;


/* thanks to mezz and JEI project for sharing source, which was reference for the getStillFluidSprite method ;) */

public abstract class FluidWidget extends WidgetGLWorld implements IFluid {
    private FluidStack fluidStack = null;

    public FluidWidget(){
        super();
        width = height = 16;
    }

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        if(fluidStack != null) {
            ByteBufUtils.writeTag(buff, fluidStack.writeToNBT(new NBTTagCompound()));
        }
        else
            ByteBufUtils.writeTag(buff, new NBTTagCompound());
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        NBTTagCompound fluid = ByteBufUtils.readTag(buff);
        if(new NBTTagCompound().equals(fluid))
            setFluid(null);
        else
            setFluid(FluidStack.loadFluidStackFromNBT(fluid));
    }

    @Override
    public void setFluid(FluidStack newStack) {
        fluidStack = newStack;
    }

    @Override
    public Fluid getFluid() {
        return fluidStack != null ? fluidStack.getFluid() : null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableFluidIcon();
    }

    public class RenderableFluidIcon extends RenderableGLWidget{
        Minecraft mc = Minecraft.getMinecraft();

        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(getFluid() == null)
                return;

            TextureAtlasSprite fluidSprite = getStillFluidSprite(getFluid());

            GL11.glPushMatrix();

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
                //GL11.glTranslatef(0F, 1F, 0F);
                //GL11.glRotated(180, 1, 0, 0);
            }


            TextureManager tm = mc.getTextureManager();

            tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tm.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

            //setGLColorFromInt(getFluid().getColor());

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferBuilder.pos(renderOffset.x, renderOffset.y + height, 1).tex(fluidSprite.getMinU(), fluidSprite.getMaxV()).endVertex();
            bufferBuilder.pos(renderOffset.x + width, renderOffset.y + height, 1).tex(fluidSprite.getMaxU(), fluidSprite.getMaxV()).endVertex();
            bufferBuilder.pos(renderOffset.x + width, renderOffset.y, 1).tex(fluidSprite.getMaxU(), fluidSprite.getMinV()).endVertex();
            bufferBuilder.pos(renderOffset.x, renderOffset.y, 1).tex(fluidSprite.getMinU(), fluidSprite.getMinV()).endVertex();
            tessellator.draw();

            this.postRender();
            GL11.glPopMatrix();
        }

        private void setGLColorFromInt(int color) {
            float red = (color >> 16 & 0xFF) / 255.0F;
            float green = (color >> 8 & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;

            GlStateManager.color(red, green, blue, 1.0F);
        }


        private TextureAtlasSprite getStillFluidSprite(Fluid fluid) {
            if (fluid.getStill() != null)
                return mc.getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());

            return mc.getTextureMapBlocks().getMissingSprite();
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

    }

}


