package ben_mkiv.guitoolkit.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class ImageButton extends prettyButton {
    private boolean isToggleButton;
    private boolean toggle;
    private ResourceLocation texture;

    public ImageButton(int id, int posx, int posy, int width, int height, String text, ResourceLocation texture , boolean isToggleButton) {
        super(id, posx, posy, width, height, text);
        this.isToggleButton=isToggleButton;
        this.texture=texture;
        this.toggle=false;
    }

    public ImageButton(int id, int posx, int posy, int width, int height, ResourceLocation texture, String text) {
        super(id, posx, posy, width, height, text);
        this.isToggleButton=false;
        this.texture=texture;
        this.toggle=false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (!this.visible)
            return;

        Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);

        this.hovered = (mouseX<=this.x+this.width) && (mouseX>=this.x) && (mouseY<=this.y+this.height) && (mouseY>=this.y);

        Tessellator tes = Tessellator.getInstance();

        double v0 = (this.hovered && this.enabled) ? 0.5 : 0;
        double v1 = v0 + 0.5;
        double u0 = (this.toggle) ? 0.5 : 0;
        double u1 =  u0 +((this.isToggleButton) ? 0.5 : 1);

        BufferBuilder vertexbuffer = tes.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(this.x, this.y + this.height, this.zLevel).tex(u0, v1).endVertex();
        vertexbuffer.pos(this.x + this.width, this.y + this.height , this.zLevel).tex(u1, v1).endVertex();
        vertexbuffer.pos(this.x + this.width, this.y , this.zLevel).tex(u1, v0).endVertex();
        vertexbuffer.pos(this.x, this.y , this.zLevel).tex(u0, v0).endVertex();
        tes.draw();

        if(this.displayString != null && !this.displayString.equals("")){
            int color = (!this.enabled) ? 0xA0A0A0 : ((this.hovered) ? 0xFFFFA0 : 0xE0E0E0);
            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.displayString, this.x+this.width/2, this.y+(this.height-8)/2, color);
        }
    }

    public void setToggle(boolean toggle){
        this.toggle=toggle;
    }

    public boolean getToggle(){
        return this.toggle;
    }


}