package ben_mkiv.guitoolkit.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public abstract class prettyGuiList extends GuiScrollingList {
    int selected;

    public prettyGuiList(int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight){
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
    }

    @Override
    protected void drawBackground() {}

    public class ListSlider extends GuiSlider {

        ISliderNotStupid callback;
        int listIndex = -1;

        public ListSlider(int id, int idInternal, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, @Nullable ISliderNotStupid callback) {
            super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
            this.callback = callback;
            this.listIndex = idInternal;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks){
            Minecraft client = Minecraft.getMinecraft();
            int viewHeight     = bottom - top;

            ScaledResolution res = new ScaledResolution(client);
            double scaleW = client.displayWidth / res.getScaledWidth_double();
            double scaleH = client.displayHeight / res.getScaledHeight_double();

            int x = (int)(left * scaleW);
            int y = (int)(client.displayHeight - (bottom * scaleH));
            int width = (int)(listWidth * scaleW);
            int height = (int)(viewHeight * scaleH);

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(x, y, width, height);

            super.drawButton(mc, mouseX, mouseY, partialTicks);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            // add an additional check to the hover flag to check if its within the list boundary
            hovered&= mouseY < bottom;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){
            return mouseY < bottom && super.mousePressed(mc, mouseX, mouseY);
        }

        @Override
        public void mouseReleased(int par1, int par2){
            if(this.dragging){
                this.dragging = false;
                if(callback != null)
                    callback.onChangeSliderValue(this, listIndex);
            }
        }


    }

    public interface ISliderNotStupid {
        void onChangeSliderValue(GuiSlider slider, int index);
    }


    @Override
    public int getSize(){ return 0;}

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        selected = index;
        //container.sampleSelected(index);
    }

    @Override
    protected boolean isSelected(int index) {
        return selected == index;
    }

    public int getSelected(){
        return selected;
    }

}
