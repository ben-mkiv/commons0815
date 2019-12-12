package ben_mkiv.rendertoolkit.client.thermalvision;

import ben_mkiv.rendertoolkit.client.OptifineHelper;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShaderHelper {
    private static boolean isActive = false;

    private static ResourceLocation resourceLocationShaderOverlay = new ResourceLocation(renderToolkit.MODID, "shaders/post/thermal_overlay.json");
    private static ResourceLocation resourceLocationShaderBlur = new ResourceLocation(renderToolkit.MODID, "shaders/post/thermal_blur.json");

    private static ScaledResolution framebufferResolution;

    private static ThermalEntityRender renderEventHandler = new ThermalEntityRender();

    private static ThermalEntityRenderer thermalEntityRendererOverlay = null;
    static ThermalEntityRenderer thermalEntityRendererBlur = null;

    public static void setupThermalShader(boolean activate){
        if (!OpenGlHelper.shadersSupported)
            return;

        if(activate) {
            if(thermalEntityRendererOverlay == null || thermalEntityRendererBlur == null)
                resetFramebuffers();

            MinecraftForge.EVENT_BUS.register(renderEventHandler);

            if(renderToolkit.Optifine && OptifineHelper.isFastRenderEnabled() && fastRenderNotificationTimeout != -1)
                fastRenderNotificationTimeout = System.currentTimeMillis() + 7000;

        }
        else {
            MinecraftForge.EVENT_BUS.unregister(renderEventHandler);
            if(fastRenderNotificationTimeout != -1)
                fastRenderNotificationTimeout = 0;
        }

        isActive = activate;
    }

    public static void resetFramebuffers(){
        Minecraft mc = Minecraft.getMinecraft();

        framebufferResolution = new ScaledResolution(mc);

        thermalEntityRendererOverlay = new ThermalEntityRenderer(resourceLocationShaderOverlay, mc.getFramebuffer());
        thermalEntityRendererBlur = new ThermalEntityRenderer(resourceLocationShaderBlur, new Framebuffer(mc.displayWidth, mc.displayHeight, true));
    }

    public static void render(RenderGameOverlayEvent event){
        if(thermalEntityRendererOverlay == null || thermalEntityRendererBlur == null)
            return;


        if(event.getResolution().getScaledWidth() != framebufferResolution.getScaledWidth()
            || event.getResolution().getScaledHeight() != framebufferResolution.getScaledHeight()){
            resetFramebuffers();
        }

        ThermalEntityRender.fastRenderEnabled &= !renderToolkit.Optifine || OptifineHelper.isFastRenderEnabled();

        if(!ThermalEntityRender.fastRenderEnabled) {
            // apply color overlay shader
            thermalEntityRendererOverlay.getShaderGroup().render(event.getPartialTicks());

            // apply blur to entities
            thermalEntityRendererBlur.getShaderGroup().render(event.getPartialTicks());
        }

        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.enableBlend();

        thermalEntityRendererBlur.getShaderGroup().getFramebufferRaw("final").framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);

        if(!ThermalEntityRender.fastRenderEnabled)
            thermalEntityRendererBlur.getShaderGroup().getFramebufferRaw("in").framebufferClear();

        GlStateManager.disableBlend();
    }

    public static boolean isActive(){
        return isActive;
    }

    private static long fastRenderNotificationTimeout = 0;

    public static void renderFastRenderNotification(){
        if(!ThermalEntityRender.fastRenderEnabled || fastRenderNotificationTimeout <= 0)
            return;

        if(System.currentTimeMillis() > fastRenderNotificationTimeout)
            fastRenderNotificationTimeout = -1;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        String text = "ThermalVision is best experienced with Optifine FastRender disabled";
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        int x = res.getScaledWidth() / 2 - fr.getStringWidth(text) / 2;
        int y = res.getScaledHeight() / 2 - 2 * fr.FONT_HEIGHT;
        fr.drawString(text, x, y, 0xFFFFFF);
    }

}
