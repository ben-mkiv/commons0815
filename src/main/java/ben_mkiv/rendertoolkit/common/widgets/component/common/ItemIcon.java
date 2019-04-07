package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemIcon extends WidgetGLWorld implements IItem {
    private ItemStack itmStack = null;
    private IBakedModel ibakedmodel = null;
    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        if(itmStack != null) {
            ByteBufUtils.writeUTF8String(buff, itmStack.getItem().getRegistryName().toString());
            buff.writeInt(itmStack.getMetadata());
        }
        else
            ByteBufUtils.writeUTF8String(buff, "none");
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        String item = ByteBufUtils.readUTF8String(buff);
        if(!item.equals("none")) {
            setItem(item, buff.readInt());
        }
    }

    @Override
    public boolean setItem(ItemStack newItem) {
        if(newItem == null || newItem.getItem() == null) return false;
        this.itmStack = newItem;
        this.ibakedmodel = null;
        return true;
    }

    @Override
    public boolean setItem(String newItem, int meta) {
        return setItem(new ItemStack(Item.getByNameOrId(newItem), 1, meta));
    }

    @Override
    public Item getItem() {
        return this.itmStack.getItem();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableItemIcon();
    }

    private class RenderableItemIcon extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(itmStack == null) return;
            if(ibakedmodel == null) ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itmStack);

            TextureManager tm = Minecraft.getMinecraft().getTextureManager();

            tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tm.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

            GlStateManager.translate(renderOffset.x, renderOffset.y, renderOffset.z);

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
                GlStateManager.translate(0F, 1F, 0F);
                GlStateManager.rotate(180, 0, 0, 1);
                GlStateManager.scale(-1, 1, 1);
            }

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.ITEM);

            for(EnumFacing facing : EnumFacing.values())
                renderQuads(vertexbuffer, ibakedmodel.getQuads(null, facing, 0L), alphaColor);

            renderQuads(vertexbuffer, ibakedmodel.getQuads(null, null, 0L), alphaColor);
            tessellator.draw();


            Minecraft.getMinecraft().fontRenderer.drawString("meow", 0, 0, 0xFFFFFF);
            this.postRender();
        }

        private void applyAlignments(){
            switch(this.getHorizontalAlign()) {
                case CENTER:
                    GlStateManager.translate(-0.5F, 0F, 0F);
                    break;
                case LEFT:
                    GlStateManager.translate(-1F, 0F, 0F);
                    break;
            }

            switch(this.getVerticalAlign()) {
                case MIDDLE:
                    GlStateManager.translate(0F, -0.5F, 0F);
                    break;
                case TOP:
                    GlStateManager.translate(0F, -1F, 0F);
                    break;
            }
        }

        private  void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color) {
            for (int j = quads.size(), i = 0; i < j; ++i)
                LightUtil.renderQuadColor(renderer, quads.get(i), color);
        }
    }

}


