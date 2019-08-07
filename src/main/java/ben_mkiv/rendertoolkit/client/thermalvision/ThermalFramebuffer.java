package ben_mkiv.rendertoolkit.client.thermalvision;

import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;


public class ThermalFramebuffer extends Framebuffer {
    ThermalFramebuffer(int width, int height, boolean useDepthIn){
        super(width, height, useDepthIn);
    }

    static float[] matrix;

    static org.lwjgl.util.vector.Matrix4f projectionMatrix = new Matrix4f();

    public static final VazkiiShaderHelper.ShaderCallback callback = shader -> {

        int loc = ARBShaderObjects.glGetUniformLocationARB(shader, "DiffuseSampler");
        //int tex = thermalFramebuffer.framebufferTexture;
        //int tex = framebufferEntities.framebufferTexture;

        int loc2 = ARBShaderObjects.glGetUniformLocationARB(shader, "InSize");
        int loc3 = ARBShaderObjects.glGetUniformLocationARB(shader, "OutSize");

        int loc4 = ARBShaderObjects.glGetUniformLocationARB(shader, "ProjMat");

        Minecraft mc = Minecraft.getMinecraft();


        //ARBShaderObjects.glUniform2fARB(loc2, (float) thermalFramebuffer.framebufferTextureWidth, (float) thermalFramebuffer.framebufferTextureHeight);
        //ARBShaderObjects.glUniform2fARB(loc3, (float) thermalFramebuffer.framebufferTextureWidth, (float) thermalFramebuffer.framebufferTextureHeight);

        ARBShaderObjects.glUniform2fARB(loc2, (float) mc.displayWidth, (float) mc.displayHeight);
        ARBShaderObjects.glUniform2fARB(loc3, (float) mc.displayWidth, (float) mc.displayHeight);

        ARBShaderObjects.glUniformMatrix4ARB(loc4, true, BufferUtils.createFloatBuffer(16).put(matrix));

        //ARBShaderObjects.glUniform1iARB(loc, tex);
    };

    private void setMatrix(){
        projectionMatrix = new org.lwjgl.util.vector.Matrix4f();
        projectionMatrix.setIdentity();
        projectionMatrix.m00 = 2.0F / (float) framebufferTextureWidth;
        projectionMatrix.m11 = 2.0F / (float)(-framebufferTextureHeight);
        projectionMatrix.m22 = -0.0020001999F;
        projectionMatrix.m33 = 1.0F;
        projectionMatrix.m03 = -1.0F;
        projectionMatrix.m13 = 1.0F;
        projectionMatrix.m23 = -1.0001999F;

        matrix = new float[]{
                projectionMatrix.m00, projectionMatrix.m01, projectionMatrix.m02, projectionMatrix.m03,
                projectionMatrix.m10, projectionMatrix.m11, projectionMatrix.m12, projectionMatrix.m13,
                projectionMatrix.m20, projectionMatrix.m21, projectionMatrix.m22, projectionMatrix.m23,
                projectionMatrix.m30, projectionMatrix.m31, projectionMatrix.m32, projectionMatrix.m33
        };
    }

    static ResourceLocation shaderLoc = new ResourceLocation(renderToolkit.MODID, "shaders/post/thermal_blur.json");

    @Override
    public void framebufferRenderExt(int width, int height, boolean p_178038_3_)
    {
        if (!OpenGlHelper.isFramebufferEnabled())
            return;

        setMatrix();

        //framebufferEntities.bindFramebuffer(false);
        //VazkiiShaderHelper.useShader(VazkiiShaderHelper.thermalBlur, callback);
        //framebufferEntities.bindFramebufferTexture();

        //framebufferEntities.unbindFramebufferTexture();
        //framebufferEntities.unbindFramebuffer();
        //VazkiiShaderHelper.releaseShader();

        //bindFramebuffer(p_178038_3_);
        //GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        //framebufferEntities.bindFramebufferTexture();
        //framebufferEntities.framebufferRenderExt(width, height, false);
        //VazkiiShaderHelper.releaseShader();

        //framebufferEntities.bindFramebufferTexture();

        //bindFramebufferTexture();
        //Minecraft.getMinecraft().entityRenderer.loadShader(shaderLoc);
        //Minecraft.getMinecraft().entityRenderer.loadShader();

        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.viewport(0, 0, width, height);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = (float)width;
        float f1 = (float)height;
        float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
        float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double)f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos((double)f, (double)f1, 0.0D).tex((double)f2, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos((double)f, 0.0D, 0.0D).tex((double)f2, (double)f3).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        //framebufferEntities.unbindFramebufferTexture();
        GlStateManager.depthMask(true);

        VazkiiShaderHelper.releaseShader();

        /*
        if(true) return;



        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.colorMask(true, true, true, true);


        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.viewport(0, 0, width, height);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();


        f = (float)width;
        f1 = (float)height;
        f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
        f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.enableColorMaterial();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        tessellator = Tessellator.getInstance();
        bufferbuilder = tessellator.getBuffer();

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //Minecraft.getMinecraft().getFramebuffer().unbindFramebuffer();

        if(false) {

            framebufferEntities.bindFramebuffer(true);
            framebufferEntities.bindFramebufferTexture();
            framebufferEntities.framebufferRenderExt(width, height, false);

            if(true) {
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(0.0D, (double) f1, 0.0D).tex(0.0D, 0.0D).endVertex();
                bufferbuilder.pos((double) f, (double) f1, 0.0D).tex((double) f2, 0.0D).endVertex();
                bufferbuilder.pos((double) f, 0.0D, 0.0D).tex((double) f2, (double) f3).endVertex();
                bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) f3).endVertex();
                tessellator.draw();
            }

            framebufferEntities.unbindFramebuffer();
        }

        if(true) {
            //Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
            bindFramebuffer(false);
            //ShaderHelper.framebufferEntities.bindFramebufferTexture();
            bindFramebufferTexture();
            //VazkiiShaderHelper.useShader(VazkiiShaderHelper.thermalBlur, callback);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, (double) f1, 0.0D).tex(0.0D, 0.0D).endVertex();
            bufferbuilder.pos((double) f, (double) f1, 0.0D).tex((double) f2, 0.0D).endVertex();
            bufferbuilder.pos((double) f, 0.0D, 0.0D).tex((double) f2, (double) f3).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) f3).endVertex();
            tessellator.draw();
            //VazkiiShaderHelper.releaseShader();

            //unbindFramebuffer();
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        */
    }
}
