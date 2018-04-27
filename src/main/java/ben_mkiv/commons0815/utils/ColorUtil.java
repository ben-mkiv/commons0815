package ben_mkiv.commons0815.utils;

import java.awt.*;

public class ColorUtil {
    public static int getIntFromColor(float red, float green, float blue, float alpha){
        Color col = new Color(red, green, blue, alpha);
        return col.getRGB();
    }
}
