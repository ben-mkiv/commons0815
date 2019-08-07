package ben_mkiv.rendertoolkit.client.thermalvision;

import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.Minecraft;
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
    public static boolean isActive = false;

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
        }
        else {
            MinecraftForge.EVENT_BUS.unregister(renderEventHandler);
        }

        isActive = activate;
    }

    private static void resetFramebuffers(){
        Minecraft mc = Minecraft.getMinecraft();

        framebufferResolution = new ScaledResolution(mc);

        thermalEntityRendererOverlay = new ThermalEntityRenderer(resourceLocationShaderOverlay, mc.getFramebuffer());
        thermalEntityRendererBlur = new ThermalEntityRenderer(resourceLocationShaderBlur, new Framebuffer(framebufferResolution.getScaledWidth(), framebufferResolution.getScaledHeight(), true));
    }

    public static void render(RenderGameOverlayEvent event){
        if(thermalEntityRendererOverlay == null || thermalEntityRendererBlur == null)
            return;

        if(event.getResolution().getScaledWidth() != framebufferResolution.getScaledWidth()
            || event.getResolution().getScaledHeight() != framebufferResolution.getScaledHeight()){
            resetFramebuffers();
        }

        // apply color overlay shader
        thermalEntityRendererOverlay.render(event.getPartialTicks());

        // apply blur to entities
        thermalEntityRendererBlur.getShaderGroup().render(event.getPartialTicks());

        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.enableBlend();

        thermalEntityRendererBlur.getShaderGroup().getFramebufferRaw("final").framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
        thermalEntityRendererBlur.getShaderGroup().getFramebufferRaw("in").framebufferClear();
    }

}
