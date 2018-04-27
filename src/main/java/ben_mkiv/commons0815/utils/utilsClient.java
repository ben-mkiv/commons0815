package ben_mkiv.commons0815.utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class utilsClient {
    public static Minecraft getMC(){ return Minecraft.getMinecraft(); }

    public static FontRenderer fontRenderer(){
        return utilsClient.getMC().fontRenderer;
    }

    public static float defaultFOV = Minecraft.getMinecraft().gameSettings.fovSetting;

    public static void setFOV(float fov){
        Minecraft.getMinecraft().gameSettings.fovSetting = fov;
    }
}
