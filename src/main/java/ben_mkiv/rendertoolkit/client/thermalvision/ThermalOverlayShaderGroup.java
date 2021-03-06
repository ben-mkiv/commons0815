package ben_mkiv.rendertoolkit.client.thermalvision;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;

public class ThermalOverlayShaderGroup extends ShaderGroup {
    private HashMap<String, Integer> shaderMap = new HashMap<>();

    public ThermalOverlayShaderGroup(TextureManager p_i1050_1_, IResourceManager resourceManagerIn, Framebuffer mainFramebufferIn, ResourceLocation p_i1050_4_) throws JsonException, IOException, JsonSyntaxException {
        super(p_i1050_1_, resourceManagerIn, mainFramebufferIn, p_i1050_4_);
    }

    @Override
    public Shader addShader(String programName, Framebuffer framebufferIn, Framebuffer framebufferOut) throws JsonException, IOException {
        Shader shader = super.addShader(programName, framebufferIn, framebufferOut);
        if(shaderMap == null)
            shaderMap = new HashMap<>();

        shaderMap.put(programName, shader.getShaderManager().getProgram());

        return shader;
    }

    public int getProgramId(String shaderName){
        return shaderMap != null && shaderMap.containsKey(shaderName) ? shaderMap.get(shaderName) : -1;
    }


}
