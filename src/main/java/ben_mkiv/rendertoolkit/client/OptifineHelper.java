package ben_mkiv.rendertoolkit.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.IntBuffer;

public class OptifineHelper {
    private static int depthBuffer = Integer.MAX_VALUE;
    private static int shaderObject = 36071;
    private static int optifineFramebuffer = Integer.MAX_VALUE;
    private static int optifineShadowFramebuffer = Integer.MAX_VALUE;
    private static IntBuffer optifineDrawbuffers;
    private static Field shaderPackLoadedField;
    private static Field isShadowPassField;

    private static Field activeShaderField;

    private static Class optifineShadersClass;

    public static boolean bindOptifineDepthBuffer(){
        if(getOptifineDepthBufferLocation() == Integer.MAX_VALUE)
            return false;

        //copy depth buffer to current gl context
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, getOptifineDepthBufferLocation(), 0);
        return true;
    }

    public static void releaseShaderProgram(){
        //shaderObject = GL11.glGetInteger(ARBShaderObjects.GL_SHADER_OBJECT_ARB);

        //int currentFB = GL11.glGetInteger(GL11.GL_DRAW_BUFFER);



        // unbind shader program
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public static void bindOptifineShadowFramebuffer(){
        ARBShaderObjects.glUseProgramObjectARB(shaderObject);

        if(getOptifineShadowFramebufferLocation() != Integer.MAX_VALUE){
            EXTFramebufferObject.glBindFramebufferEXT(36160, getOptifineShadowFramebufferLocation());
            GL20.glDrawBuffers(0);
            GL11.glReadBuffer(0);
        }

        if(getOptifineDrawbuffers() != null){
            GL20.glDrawBuffers(getOptifineDrawbuffers());
            GL11.glReadBuffer(0);
        }

    }

    public static void bindOptifineFramebuffer(){
        // rebind previous FB
        //EXTFramebufferObject.glBindFramebufferEXT(OpenGlHelper.GL_FRAMEBUFFER, drawFboId);

        if(getActiveProgram() != Integer.MAX_VALUE)
            ARBShaderObjects.glUseProgramObjectARB(getActiveProgram());

        if(getOptifineFramebufferLocation() != Integer.MAX_VALUE){
            EXTFramebufferObject.glBindFramebufferEXT(36160, getOptifineFramebufferLocation());
            GL20.glDrawBuffers(0);
            GL11.glReadBuffer(0);
        }

        if(getOptifineDrawbuffers() != null){
            GL20.glDrawBuffers(getOptifineDrawbuffers());
            GL11.glReadBuffer(0);
        }

        /*
        try {
            Field usedColorBuffers = optifineShadersClass.getDeclaredField("usedColorBuffers");
            usedColorBuffers.setAccessible(true);

            for(int i = 0; i < (int) usedColorBuffers.get(optifineShadersClass); ++i) {


                try {
                    Field dfbColorTexturesA_field = optifineShadersClass.getDeclaredField("dfbColorTexturesA");
                    dfbColorTexturesA_field.setAccessible(true);
                    int[] dfbColorTexturesA = (int []) dfbColorTexturesA_field.get(optifineShadersClass);

                    EXTFramebufferObject.glFramebufferTexture2DEXT(36160, '賠' + i, 3553, dfbColorTexturesA[i], 0);
                } catch (Exception ex){}


            }

        } catch (Exception ex){}
        */

        //EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);

        //beginTranslucent();

        //if(drawBuffers != null)
        //    GL20.glDrawBuffers(drawBuffers);

        //OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, shaderObject);

        //OpenGlHelper.glBindBuffer(OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER, readBuff);
        //OpenGlHelper.glBindBuffer(OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER, drawBuff);

        //OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, readFboId);
    }

    public static boolean isShadowPass(){
        if(isShadowPassField == null) {
            try {
                isShadowPassField = optifineShadersClass.getDeclaredField("isShadowPass");
                isShadowPassField.setAccessible(true);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine isShadowPass field failed");
            }
        }

        if(isShadowPassField != null){
            try {
                return (boolean) isShadowPassField.get(optifineShadersClass);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine isShadowPass failed");
            }
        }

        return false;
    }


    public static int getOptifineDepthBufferLocation(){
        if(depthBuffer == Integer.MAX_VALUE) {
            try {
                Field field = optifineShadersClass.getDeclaredField("dfbDepthTextures");
                field.setAccessible(true);
                IntBuffer o = (IntBuffer) field.get(optifineShadersClass);
                depthBuffer = o.get(0);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for dfbDepthTextures");
            }
        }

        return depthBuffer;
    }

    private static int getOptifineFramebufferLocation(){
        if(optifineFramebuffer == Integer.MAX_VALUE) {
            try {
                Field field = optifineShadersClass.getDeclaredField("dfb");
                field.setAccessible(true);
                optifineFramebuffer = (int) field.get(optifineShadersClass);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for dfb");
            }
        }

        return optifineFramebuffer;
    }

    private static int getOptifineShadowFramebufferLocation(){
        if(optifineShadowFramebuffer == Integer.MAX_VALUE) {
            try {
                Field field = optifineShadersClass.getDeclaredField("sfb");
                field.setAccessible(true);
                optifineShadowFramebuffer = (int) field.get(optifineShadersClass);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for sfb");
            }
        }

        return optifineShadowFramebuffer;
    }

    private static IntBuffer getOptifineDrawbuffers(){
        if(optifineDrawbuffers == null) {
            try {
                Field dfbDrawBuffers = optifineShadersClass.getDeclaredField("dfbDrawBuffers");
                dfbDrawBuffers.setAccessible(true);
                optifineDrawbuffers = (IntBuffer) dfbDrawBuffers.get(optifineShadersClass);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for dfbDrawBuffers");
            }
        }

        return optifineDrawbuffers;
    }

    public static Class getOptifineShadersClass(){
        if(optifineShadersClass == null){
            try {
                optifineShadersClass = Class.forName("net.optifine.shaders.Shaders");
            }
            catch (Exception ex){
                System.out.println("failed to retrieve optifine shader class");
            }
        }

        return optifineShadersClass;
    }

    public static int getActiveProgram(){
        int activeProgram = Integer.MAX_VALUE;

        if(activeShaderField == null)
        {
            try {
                activeShaderField = optifineShadersClass.getDeclaredField("activeProgramID");
                activeShaderField.setAccessible(true);
            }
            catch (Exception ex){
                System.out.println("failed to retrieve optifine shader activeProgramID field");
            }
        }

        if(shaderPackLoadedField != null){
            try {
                activeProgram = (int) activeShaderField.get(optifineShadersClass);
            }
            catch (Exception ex){
                System.out.println("failed to retrieve optifine activeProgramID");
            }
        }


        return activeProgram;
    }

    public static boolean isShaderActive(){
        boolean isActive = false;

        if(shaderPackLoadedField == null)
        {
            try {
                shaderPackLoadedField = optifineShadersClass.getDeclaredField("shaderPackLoaded");
                shaderPackLoadedField.setAccessible(true);
            }
            catch (Exception ex){
                System.out.println("failed to retrieve optifine shader state field");
            }
        }

        if(shaderPackLoadedField != null){
            try {
                isActive = (boolean) shaderPackLoadedField.get(optifineShadersClass);
            }
            catch (Exception ex){
                System.out.println("failed to retrieve optifine shader state");
            }
        }


        return isActive;
    }

}
