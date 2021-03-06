package ben_mkiv.rendertoolkit.client.shaders;

/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 9, 2014, 11:20:26 PM (GMT)]
 *
 * this file is a modified version of the botania shader helper class which was based on the LWJGL Shader Tutorial, thanks to vazkii
 * mostly stripped of botania content
 */


import ben_mkiv.rendertoolkit.client.thermalvision.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class VazkiiShaderHelper {

    private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
    private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

    static HashSet<ShaderTemplate> shaders = new HashSet<>();

    static class ShaderTemplate {
        public String vertexShader;
        public String fragmentShader;
        public int index;

        public ShaderTemplate(String vertexShaderLocation, String fragmentShaderLocation, ShaderCallback callback){
            vertexShader = vertexShaderLocation;
            fragmentShader = fragmentShaderLocation;
            callback.call(createProgram(vertexShaderLocation, fragmentShaderLocation));
        }
    }

    @FunctionalInterface
    public interface ShaderCallback {
        void call(int shader);
    }

    private static boolean lighting;

    public static int getShaderIndex(@Nonnull String vertexShaderLocation, @Nonnull String fragmentShaderLocation){
        for(ShaderTemplate shader : shaders) {
            if (shader.fragmentShader.equals(fragmentShaderLocation) && shader.vertexShader.equals(vertexShaderLocation))
                return shader.index;
        }

        return -1;
    }

    public static void initShader(@Nonnull String vertexShaderLocation, @Nonnull String fragmentShaderLocation, ShaderCallback shaderInitCallback) {
        if(getShaderIndex(vertexShaderLocation, fragmentShaderLocation) != -1)
            return;

        if (Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
            ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(manager -> {
                if(useShaders()) {
                    shaders.add(new ShaderTemplate(vertexShaderLocation, fragmentShaderLocation, shaderInitCallback));
                }
            });
        }
    }


    public static void useShader(int shader, ShaderCallback callback) {
        if(!useShaders() || shader == 0)
            return;

        lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();

        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(callback != null)
            callback.call(shader);
    }

    public static void releaseShader() {
        if(lighting)
            GlStateManager.enableLighting();

        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public static boolean useShaders() {
        return OpenGlHelper.shadersSupported; // && !renderToolkit.Optifine;
    }


    public static int createProgram(String vert, String frag) {
        int program = ARBShaderObjects.glCreateProgramObjectARB();

        if(program == 0)
            return 0;

        if(!vert.equals("")) {
            ARBShaderObjects.glAttachObjectARB(program, createShader(vert, VERT));
        }
        if(!frag.equals("")) {
            ARBShaderObjects.glAttachObjectARB(program, createShader(frag, FRAG));
        }

        ARBShaderObjects.glLinkProgramARB(program);

        if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.out.println(getLogInfo(program));
            return 0;
        }

        ARBShaderObjects.glValidateProgramARB(program);

        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.out.println(getLogInfo(program));
            return 0;
        }

        return program;
    }

    private static int createShader(String filename, int shaderType){
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if(shader == 0)
                return 0;


            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

            return shader;
        }
        catch(Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            e.printStackTrace();
            return -1;
        }
    }

    private static String getLogInfo(int shader) {
        return ARBShaderObjects.glGetInfoLogARB(shader, ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private static String readFileAsString(String filename) throws Exception {
        InputStream in = ShaderHelper.class.getResourceAsStream(filename);

        if(in == null)
            return "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

}
