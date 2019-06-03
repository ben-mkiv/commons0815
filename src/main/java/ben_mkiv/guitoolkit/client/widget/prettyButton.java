package ben_mkiv.guitoolkit.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class prettyButton extends GuiButton implements prettyElement {
    public enum alignment {LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM}

    public int marginLeft = 0, marginRight = 0, marginTop = 0, marginBottom = 0;

    public alignment
            verticalAlignment = alignment.MIDDLE,
            horizontalAlignment = alignment.CENTER;

    public ArrayList<String> tooltip = new ArrayList<>();

    public String action;

    public int renderX = 0, renderY = 0;

    public prettyButton(int id, int x, int y, int width, int height, String label) {
        super(id, x, y, width, height, label);
        action = label;
    }

    public boolean renderTooltip(Minecraft mc, int mx, int my) {
        if (tooltip.size() < 1)
            return false;

        if (!this.visible)
            return false;

        if (!this.isPointInRegion(mx, my))
            return false;

        return true;
    }

    @Override // Interface prettyElement
    public int getY() {
        return this.y;
    }

    @Override // Interface prettyElement
    public int getX() {
        return this.x;
    }

    @Override // Interface prettyElement
    public int getWidth() {
        return this.width;
    }

    @Override // Interface prettyElement
    public int getHeight() {
        return this.height;
    }

    @Override // Interface prettyElement
    public void setY(int pos) {
        this.y = pos;
    }

    @Override // Interface prettyElement
    public void setX(int pos) {
        this.x = pos;
    }

    @Override // Interface prettyElement
    public void setRenderY(int pos) {
        this.renderY = pos;
    }

    @Override // Interface prettyElement
    public void setRenderX(int pos) {
        this.renderX = pos;
    }

    @Override // Interface prettyElement
    public void setVisible(boolean isVisible) {
        visible = isVisible;
    }

    @Override // Interface prettyElement
    public boolean getVisible() {
        return visible;
    }

    protected boolean isPointInRegion(int x, int y) {
        return !(x < this.x + this.renderX || x > this.x + this.width + this.renderX || y < this.y + this.renderY || y > this.y + this.height + this.renderY);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks){
        if (!this.visible)
            return;

        FontRenderer fontrenderer = mc.fontRenderer;
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.hovered = isPointInRegion(mouseX, mouseY);

        int i = this.getHoverState(this.hovered);

        int renderX = this.renderX + this.x;
        int renderY = this.renderY + this.y;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.drawTexturedModalRect(renderX, renderY, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(renderX + this.width / 2, renderY, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

        this.mouseDragged(mc, mouseX, mouseY);

        int textColor = 14737632;

        if (packedFGColour != 0)
            textColor = packedFGColour;
        else if (!this.enabled)
            textColor = 10526880;
        else if (this.hovered)
            textColor = 16777120;

        int posX, posY;

        switch(horizontalAlignment){
            case RIGHT:
                posX = -marginRight + renderX + this.width - (fontrenderer.getStringWidth(this.displayString) / 2);
                break;
            case LEFT:
                posX = marginLeft + renderX + (fontrenderer.getStringWidth(this.displayString) / 2);
                break;
            case CENTER:
            default:
                posX = renderX + this.width / 2; break;
        }

        switch(verticalAlignment){
            case BOTTOM:
                posY = -marginBottom + renderY + this.height - fontrenderer.FONT_HEIGHT;
                break;
            case TOP:
                posY = marginTop + renderY;
                break;
            case MIDDLE:
            default:
                posY = renderY + (this.height-fontrenderer.FONT_HEIGHT) / 2; break;
        }


        this.drawCenteredString(fontrenderer, this.displayString, posX, posY, textColor);

    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){
        return this.enabled && this.visible && isPointInRegion(mouseX, mouseY);
    }


}
