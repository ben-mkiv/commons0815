package ben_mkiv.guitoolkit.client.widget;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class PrettyTextField extends GuiTextField {

    private String emptyVal = "";
    Predicate<String> theValidator = Predicates.alwaysTrue();

    public PrettyTextField(int componentId, int x, int y, int width, int height){
        this(componentId, x, y, width, height, "");
    }

    public PrettyTextField(int componentId, int x, int y, int width, int height, String emptyText){
        super(componentId, Minecraft.getMinecraft().fontRenderer, x, y, width, height);
        this.emptyVal=emptyText;
    }

    @Override
    public void setFocused(boolean hasFocus){
        super.setFocused(hasFocus);

        if(emptyVal.length() > 0) {
            super.setValidator(Predicates.alwaysTrue());

            if (hasFocus && getText().equals(emptyVal))
                setText("");
            else if(!hasFocus && getText().length() == 0)
                setText(emptyVal);

            super.setValidator(theValidator);
        }
    }

    @Override
    public void setValidator(Predicate<String> theValidator) {
        super.setValidator(theValidator);
        this.theValidator = theValidator;
    }

}
