package ben_mkiv.rendertoolkit.client;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.lang.reflect.Field;
import java.nio.IntBuffer;

public class OptifineHelper {
    private static int depthBuffer = Integer.MAX_VALUE;
    private static int shaderObject = 36071;
    private static int optifineFramebuffer = Integer.MAX_VALUE;
    private static IntBuffer optifineDrawbuffers;

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

        // unbind shader program
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public static void bindOptifineFramebuffer(){
        // rebind previous FB
        //EXTFramebufferObject.glBindFramebufferEXT(OpenGlHelper.GL_FRAMEBUFFER, drawFboId);

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

        if(false) try {
            Method setupFrameBuffer = Shaders.class.getDeclaredMethod("setupFrameBuffer");
            setupFrameBuffer.setAccessible(true);
            setupFrameBuffer.invoke(null);
        } catch (Exception ex){}



        if(false) try {
            Field usedColorBuffers = Shaders.class.getDeclaredField("usedColorBuffers");
            usedColorBuffers.setAccessible(true);

            for(int i = 0; i < (int) usedColorBuffers.get(Shaders.class); ++i) {


                try {
                    Field dfbColorTexturesA_field = Shaders.class.getDeclaredField("dfbColorTexturesA");
                    dfbColorTexturesA_field.setAccessible(true);
                    int[] dfbColorTexturesA = (int []) dfbColorTexturesA_field.get(Shaders.class);

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


    private static int getOptifineDepthBufferLocation(){
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

}
