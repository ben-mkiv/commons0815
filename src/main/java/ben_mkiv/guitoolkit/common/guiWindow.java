package ben_mkiv.guitoolkit.common;

import ben_mkiv.guitoolkit.client.widget.prettyButton;
import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.component.face.Box2D;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class guiWindow extends GuiContainer {
    protected String name = "defaultName";
    public boolean hasBackground = false;

    protected ArrayList<GuiTextField> textFields = new ArrayList<>();
    static Box2D box;

    static IRenderableWidget backgroundWidget;

    static {
        box = new Box2D();
        box.setSize(2000, 2000);
        box.WidgetModifierList.addColor(0, 0, 0, 0.5F);
        box.WidgetModifierList.addColor(0.2F, 0.2F, 0.2F, 0.5F);
    }

    protected Slot hoveredSlot = null;
    protected ItemStack hoveredJEI = null;

    public guiWindow(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    public class screenLinkButton extends prettyButton {
        public Class screen;

        public screenLinkButton(int x, int y, int width, int height, String label, Class screen){
            super(buttonList.size(), x, y, width, height, label);
            this.screen = screen;
        }
    }

    @Override
    public void initGui(){
        super.initGui();
        guiTop = 0;

        backgroundWidget = box.getRenderable();
        //guiLeft = 0;
    }

    public static void renderWidget(IRenderableWidget widget, Vec3d location){
        ClientSurface.renderSingleWidgetOverlay(widget, Long.MAX_VALUE, location);

        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(0);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
    }

    public static void renderWidget(IRenderableWidget widget){
        renderWidget(widget, ClientSurface.vec3d000);
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        this.updateScreen();

        renderWidget(backgroundWidget);

        super.drawScreen(mx, my, partialTicks);
        RenderHelper.disableStandardItemLighting();

        for (GuiTextField tF : textFields)
            tF.drawTextBox();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getType(){
        return "customGUI";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if(!hasBackground)
            return;

        drawDefaultBackground();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);

        for(GuiButton button : buttonList)
            if(button instanceof prettyButton)
                if(((prettyButton) button).renderTooltip(mc, mx, my))
                    drawHoveringText(((prettyButton) button).tooltip, mx - getGuiLeft(), my - getGuiTop(), mc.fontRenderer);
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

    @SuppressWarnings( "deprecation" )
    private String translate(String s){
        return net.minecraft.util.text.translation.I18n.translateToLocal(s);
    }

    public int getXOffset(int width){
        return (guiLeft + getXSize()/2 - width/2);
    }

    public void drawStringCentered(String text, int offsetY, int color){
        drawStringCentered(text, offsetY, color, 1);
    }

    public void drawStringCentered(String text, int offsetY, int color, float scale){
        int offsetX = getXOffset(Math.round(fontRenderer.getStringWidth(text) / (1/scale)));

        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        fontRenderer.drawString(text, Math.round(offsetX*(1/scale)), Math.round(offsetY*(1/scale)), color);
        GL11.glPopMatrix();
    }
}
