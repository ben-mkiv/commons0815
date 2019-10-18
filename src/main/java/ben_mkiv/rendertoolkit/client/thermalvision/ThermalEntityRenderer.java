package ben_mkiv.rendertoolkit.client.thermalvision;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class ThermalEntityRenderer extends EntityRenderer {
    public Framebuffer framebuffer;

    public ThermalEntityRenderer(ResourceLocation shaderLocation, Framebuffer framebufferIn){
        super(Minecraft.getMinecraft(), Minecraft.getMinecraft().getResourceManager());

        framebuffer = framebufferIn;
        loadShader(shaderLocation);
    }

    @Override
    public void loadShader(ResourceLocation shaderLocation){
        try {
            Minecraft mc = Minecraft.getMinecraft();
            this.shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), framebuffer, shaderLocation);
            this.shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            this.useShader = true;
        } catch (Exception ex){
            System.out.println("failed to load shader '"+shaderLocation.toString()+"'");
            ex.printStackTrace();
        }
    }


}
