package ben_mkiv.guitoolkit.client.widget;

import ben_mkiv.guitoolkit.guiToolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class prettyCheckbox extends prettyButton {
    private static final ResourceLocation texture = new ResourceLocation(guiToolkit.MODID, "textures/gui/checkbox.png");

    boolean isEnabled;

    public prettyCheckbox(int id, int x, int y, String label, boolean status) {
        super(id, x, y, 20, 20, label);
        isEnabled = status;
    }

    public void setEnabled(boolean enabled){
        isEnabled = enabled;
    }

    public boolean isEnabled(){
        return isEnabled;
    }


    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks){
        if (!this.visible)
            return;

        int textureX = 32, textureY = 0;
        int dimension = 16;

        if(isEnabled())
            textureY+=dimension;

        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(x, y, textureX, textureY, dimension, dimension);

        if(displayString.length() > 0){
            FontRenderer fontrenderer = mc.fontRenderer;
            fontrenderer.drawString(displayString, x + dimension + 2, y+ fontrenderer.FONT_HEIGHT/2 + 2, 0x0);
        }
    }



}
