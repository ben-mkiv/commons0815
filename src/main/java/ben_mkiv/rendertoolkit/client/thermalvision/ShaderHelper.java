package ben_mkiv.rendertoolkit.client.thermalvision;

import ben_mkiv.rendertoolkit.renderToolkit;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

public class ShaderHelper {
    public enum ShaderType { VANILLA_GLOW, THERMAL_VISION }

    public static ShaderType activeShader = ShaderType.VANILLA_GLOW;

    static ThermalEntityRender renderEventHandler = new ThermalEntityRender();

    public static void loadOutlineShader(ShaderType type){
        if (!OpenGlHelper.shadersSupported || type.equals(activeShader))
            return;

        ResourceLocation resourcelocation;

        switch(type){
            case THERMAL_VISION:
                resourcelocation = new ResourceLocation(renderToolkit.MODID, "shaders/post/thermal.json");
                break;

            default:
            case VANILLA_GLOW:
                resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
                break;
        }

        Minecraft mc = Minecraft.getMinecraft();
        RenderGlobal rg = Minecraft.getMinecraft().renderGlobal;

        if(type.equals(ShaderType.VANILLA_GLOW)) {
            for (Entity entity : ThermalEntityRender.forceGlowingEntities)
                entity.setGlowing(false);

            ThermalEntityRender.forceGlowingEntities.clear();
        }

        try {
            switch(type){
                case THERMAL_VISION:
                    rg.entityOutlineShader = new ThermalShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourcelocation);
                    MinecraftForge.EVENT_BUS.register(renderEventHandler);
                    break;

                default:
                case VANILLA_GLOW:
                    rg.entityOutlineShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourcelocation);
                    MinecraftForge.EVENT_BUS.unregister(renderEventHandler);
                    break;
            }

            rg.entityOutlineShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            rg.entityOutlineFramebuffer = rg.entityOutlineShader.getFramebufferRaw("final");

            activeShader = type;
        }
        catch (IOException|JsonSyntaxException exception){
            System.out.println("Failed to replace glow shader");
            rg.entityOutlineShader = null;
            rg.entityOutlineFramebuffer = null;
        }
    }
}
