package ben_mkiv.rendertoolkit.client;

import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.IntBuffer;

import static ben_mkiv.rendertoolkit.renderToolkit.Optifine;

public class OptifineHelper {
    private static int depthBuffer = Integer.MAX_VALUE;
    private static int optifineFramebuffer = Integer.MAX_VALUE;
    private static IntBuffer optifineDrawbuffers;


    private static Program lastShaderProgram;

    private static Class optifineShadersClass = null;
    private static Class optifineConfigClass = null;

    private static MethodHandle isFastRenderMethodHandle = null;

    public static boolean bindOptifineDepthBuffer(){
        if(getOptifineDepthBufferLocation() == Integer.MAX_VALUE)
            return false;

        //copy depth buffer to current gl context
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, getOptifineDepthBufferLocation(), 0);
        return true;
    }

    public static void releaseShaderProgram(){
        // unbind shader program
        lastShaderProgram = Shaders.activeProgram;
        Shaders.useProgram(Shaders.ProgramNone);
    }

    public static void rebindShaderProgram(){
        Shaders.useProgram(lastShaderProgram);
    }

    public static void bindOptifineFramebuffer(){
        // enable previous shader program
        rebindShaderProgram();

        // rebind previous FB
        if(getOptifineFramebufferLocation() != Integer.MAX_VALUE){
            EXTFramebufferObject.glBindFramebufferEXT(36160, getOptifineFramebufferLocation());

            // rebind previous drawBuffers
            if(getOptifineDrawbuffers() != null)
                GL20.glDrawBuffers(getOptifineDrawbuffers());
            else
                GL20.glDrawBuffers(0);

            GL11.glReadBuffer(0);
        }
    }

    public static int getOptifineDepthBufferLocation(){
        if(depthBuffer == Integer.MAX_VALUE) {
            try {
                Field field = Shaders.class.getDeclaredField("dfbDepthTextures");
                field.setAccessible(true);
                IntBuffer o = (IntBuffer) field.get(Shaders.class);
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
                Field field = Shaders.class.getDeclaredField("dfb");
                field.setAccessible(true);
                optifineFramebuffer = (int) field.get(Shaders.class);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for dfb");
            }
        }

        return optifineFramebuffer;
    }

    private static IntBuffer getOptifineDrawbuffers(){
        if(optifineDrawbuffers == null) {
            try {
                Field dfbDrawBuffers = Shaders.class.getDeclaredField("dfbDrawBuffers");
                dfbDrawBuffers.setAccessible(true);
                optifineDrawbuffers = (IntBuffer) dfbDrawBuffers.get(Shaders.class);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for dfbDrawBuffers");
            }
        }

        return optifineDrawbuffers;
    }

    /*
        try {
            MethodHandle foo = MethodHandles.lookup().findGetter(optifineShadersClass, "dfbDrawBuffers", IntBuffer.class);
            return (IntBuffer) foo.invoke();
        } catch(Throwable ex){}
    * */

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

    public static Class getOptifineConfigClass(){
        if(optifineConfigClass == null){
            try {
                optifineConfigClass = Class.forName("Config", true, Shaders.class.getClassLoader());
            }
            catch (Exception ex){
                System.out.println("failed to retrieve optifine config class");
            }
        }

        return optifineConfigClass;
    }

    public static boolean isShaderActive(){
        return Shaders.shaderPackLoaded;
    }

    public static boolean isShadowPass(){
        return Shaders.isShadowPass;
    }

    public static boolean isFastRenderEnabled(){
        if(isFastRenderMethodHandle == null) {
            try {
                MethodType mt = MethodType.methodType(boolean.class);
                isFastRenderMethodHandle = MethodHandles.lookup().findStatic(getOptifineConfigClass(), "isFastRender", mt);
            } catch (Exception ex) {
                System.out.println("reflection of Optifine Shader class failed for dfbDrawBuffers");
                return false;
            }
        }

        try {
            return (boolean) isFastRenderMethodHandle.invokeExact();
        }
        catch(Throwable ex){
            return false;
        }
    }
}
