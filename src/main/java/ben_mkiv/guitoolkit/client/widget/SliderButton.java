package ben_mkiv.guitoolkit.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SliderButton {
    private ResourceLocation texture;
    private int w;
    private int h;
    private int maxh;
    private int posx;
    private int posy;
    private int slidery;
    private boolean activated = false;

    private int maxsteps = 0;
    private int scroll;
    private boolean update = false;

    public SliderButton(int posx, int posy ,int w, int h, int maxh){
        this.texture = new ResourceLocation( "opencomputers" , "textures/gui/button_scroll.png");;
        this.w = w;
        this.h = h;
        this.maxh = maxh;
        this.posx = posx;
        this.posy = posy;
        this.slidery = 0;
    }

    @SideOnly(Side.CLIENT)
    public void drawSlider(float zLevel ,boolean highlight){

        Tessellator tes = Tessellator.getInstance();

        double v0 = (highlight) ? 0.5 : 0;
        double v1 = v0 + 0.5;

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        RenderHelper.disableStandardItemLighting();

        BufferBuilder vertexbuffer = tes.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(posx + w, posy + h + slidery, zLevel).tex(1, v1).endVertex();
        vertexbuffer.pos(posx + w, posy + slidery, zLevel).tex(1, v0).endVertex();
        vertexbuffer.pos(posx, posy + slidery, zLevel).tex(0, v0).endVertex();
        vertexbuffer.pos(posx, posy + h + slidery, zLevel).tex(0, v1).endVertex();
        tes.draw();

        RenderHelper.enableStandardItemLighting();
    }

    public boolean isMouseHoverBox(int mx, int my){
        return mx >= posx - 1 && mx < posx + w + 1 && my >= posy - 1 && my < posy + maxh + 1;
    }

    public boolean isMouseHoverButton(int mx, int my){
        return mx >= posx - 1 && mx < posx + w + 1 && my >= posy + slidery - 1 && my < posy + slidery + h + 1;
    }

    public void scrollMouse(int my){
        this.scrollTo((int)Math.round((my - posy + 1 - 6.5  ) * (maxsteps-1) / (maxh - 13.0)));
    }

    public void scrollTo(int pos){
        if(pos < 0)
            this.scroll = 0;
        else if(pos > this.maxsteps)
            this.scroll = this.maxsteps;
        else
            this.scroll = pos;

        this.slidery = this.maxsteps<1 ? 0 : (this.maxh - this.h - 2) * this.scroll / this.maxsteps;
        this.update = true;
    }

    public int getScroll(){
        return this.scroll;
    }

    public void scrollDown() {
        this.scrollTo(this.scroll + 1);
    }

    public void scrollUp() {
        this.scrollTo(this.scroll - 1);
    }

	/*----------Setter/Getter---------*/

    public void setActive(boolean state){
        this.activated = state;
    }

    public boolean isActive(){
        return this.activated;
    }

    public int getMaxsteps() {
        return this.maxsteps;
    }

    public void setMaxsteps(int maxsteps) {
        if(maxsteps < 0) maxsteps = 0;
        this.maxsteps = maxsteps;
    }

    public boolean hasUpdate(){
        return this.update;
    }

    public void doneUpdate(){
        this.update = false;
    }
}