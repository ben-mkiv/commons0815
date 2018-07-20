package ben_mkiv.commons0815.utils;

import ben_mkiv.guitoolkit.common.guiWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.client.gui.Gui.ICONS;

public class ExperienceUtils {
    @SideOnly(Side.CLIENT)
    public static void drawExperienceBar(guiWindow gui, int y, int XP){
        drawExperienceBar(gui, gui.getXOffset(182), y, XP);
    }

    @SideOnly(Side.CLIENT)
    public static void drawExperienceBar(guiWindow gui, int x, int y, int XP){
        int level = getLevelFromExp(XP);

        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);

        gui.drawTexturedModalRect(x, y, 0, 64, 182, 5);
        int widthBarActive = (int) Math.round(182 * ((double) (XP-getExpFromLevel(level)) / getXpBarCap(level)));
        gui.drawTexturedModalRect(x, y, 0, 69, widthBarActive, 5);
    }

    public static int getTierFromXP(int level){
        if(level >= 40)
            return 4;
        else if(level >= 30)
            return 3;
        else if(level >= 20)
            return 2;
        else if(level >= 10)
            return 1;

        return 0;
    }

    public static int getLevelFromExp(long experience) {
        for (int i = 1; i < xpmap.length; i++) {
            if (xpmap[i] > experience) {
                return i;
            }
        }
        return xpmap.length;
    }

    /* thanks to EnderIO Team for sharing source, Experience Level related code is based on their buggy source :/ */
    private static final int[] xpmap = new int[21863];
    static {
        for (int level = 0, res = 0; level < xpmap.length; level++) {
            res += getXpBarCap(level);
            if (res < 0) {
                res = Integer.MAX_VALUE;
            }
            xpmap[level] = res;
        }
    }

    public static int getExpFromLevel(int level) {
        if (level <= 0)
            return 0;

        if (level >= 21863)
            return Integer.MAX_VALUE;

        return xpmap[level-1];
    }


    public static int getXpBarCap(int level){
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else if (level >= 15) {
            return 37 + (level - 15) * 5;
        }
        return 7 + level * 2;
    }
}
