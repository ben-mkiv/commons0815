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

        if(fluidStack != null)
            ByteBufUtils.writeTag(buff, fluidStack.writeToNBT(new NBTTagCompound()));
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

            int alphaColor = this.preRender(conditionStates);
            this.applyModifiers(conditionStates);

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
                this.applyAlignments();
            }

            TextureManager tm = mc.getTextureManager();

            tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tm.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferBuilder.pos(renderOffset.x, renderOffset.y + height, 1).tex(fluidSprite.getMinU(), fluidSprite.getMaxV()).endVertex();
            bufferBuilder.pos(renderOffset.x + width, renderOffset.y + height, 1).tex(fluidSprite.getMaxU(), fluidSprite.getMaxV()).endVertex();
            bufferBuilder.pos(renderOffset.x + width, renderOffset.y, 1).tex(fluidSprite.getMaxU(), fluidSprite.getMinV()).endVertex();
            bufferBuilder.pos(renderOffset.x, renderOffset.y, 1).tex(fluidSprite.getMinU(), fluidSprite.getMinV()).endVertex();
            tessellator.draw();

            this.postRender();
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
                    GlStateManager.translate(-0.5F, 0F, 0F);
                    break;
                case RIGHT:
                    GlStateManager.translate(-1F, 0F, 0F);
                    break;
            }

            switch(this.getVerticalAlign()) {
                case MIDDLE:
                    GlStateManager.translate(0F, -0.5F, 0F);
                    break;
                case BOTTOM:
                    GlStateManager.translate(0F, -1F, 0F);
                    break;
            }
        }

    }

}

