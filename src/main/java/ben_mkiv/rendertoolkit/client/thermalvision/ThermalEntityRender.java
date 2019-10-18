package ben_mkiv.rendertoolkit.client.thermalvision;

import ben_mkiv.rendertoolkit.client.OptifineHelper;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public class ThermalEntityRender {

    ThermalEntityRender(){
        VazkiiShaderHelper.initShaders();
    }

    private static double currentDistance = 0;
    private static boolean isDead = false;

    private static boolean overlayVisible = true;

    private static boolean isOptifineSpecialCase = false;
    protected static boolean fastRenderEnabled = false;


    public static final VazkiiShaderHelper.ShaderCallback callback = shader -> {
        if(isDead){
            setShaderColor(shader, "red", 0f);
            setShaderColor(shader, "blue", 1.0f / (float) (currentDistance / 8f));
        }
        else {
            setShaderColor(shader, "red", 1.0f / (float) (currentDistance / 8f));
            setShaderColor(shader, "blue", 0f);
        }

        setShaderColor(shader, "green", 1f - (float) (8f/currentDistance));
        setShaderColor(shader, "alpha", 1f);
    };

    private static void setShaderColor(int shader, String name, float val){
        ARBShaderObjects.glUniform1fARB(OpenGlHelper.glGetUniformLocation(shader, name), val);
    }


    @SubscribeEvent
    public void preRender(RenderLivingEvent.Pre<EntityLivingBase> event){
        Minecraft mc = Minecraft.getMinecraft();

        if(event.getEntity().equals(mc.player))
            return;

        isOptifineSpecialCase = renderToolkit.Optifine && OptifineHelper.isShaderActive();

        if(isOptifineSpecialCase && OptifineHelper.isShadowPass())
            return;

        fastRenderEnabled = !isOptifineSpecialCase && renderToolkit.Optifine && OptifineHelper.isFastRenderEnabled();

        overlayVisible = mc.gameSettings.thirdPersonView == 0 && !mc.gameSettings.hideGUI;

        if(!overlayVisible)
            return;

        isDead = false;

        if(event.getEntity() instanceof EntityMob) {
            switch (event.getEntity().getCreatureAttribute()) {
                case UNDEFINED:
                case ARTHROPOD:
                    break;
                case UNDEAD:
                case ILLAGER:
                    isDead = !event.getEntity().isBurning(); // dont set burning entities to dead :P
            }
        }

        // get distance to player
        currentDistance = event.getEntity().getPositionVector().distanceTo(mc.player.getPositionVector());

        if(isOptifineSpecialCase)
            OptifineHelper.releaseShaderProgram();

        // reenable depth testing (which gets disabled in outline renderer)
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // set thermalcolor rendershader
        VazkiiShaderHelper.useShader(VazkiiShaderHelper.thermalColorShader, callback);

        // bind to entity framebuffer
        ShaderHelper.thermalEntityRendererBlur.getShaderGroup().getFramebufferRaw("in").bindFramebuffer(true);

        if(isOptifineSpecialCase)
           OptifineHelper.bindOptifineDepthBuffer();
    }

    @SubscribeEvent
    public void postRender(RenderLivingEvent.Post<EntityLivingBase> event){
        if(isOptifineSpecialCase && OptifineHelper.isShadowPass())
            return;

        VazkiiShaderHelper.releaseShader();

        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        if(isOptifineSpecialCase) {
            OptifineHelper.bindOptifineFramebuffer();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderFogEvent(EntityViewRenderEvent.FogDensity event) {
        if(!overlayVisible)
            return;

        event.setDensity(0.03f);
        GlStateManager.setFog(GlStateManager.FogMode.EXP2);
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderFogEvent(EntityViewRenderEvent.FogColors event) {
        if(!overlayVisible)
            return;

        event.setRed(0.2f);
        event.setGreen(0.4f);
        event.setBlue(0.85f);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre evt) {
        if (evt.getType() != RenderGameOverlayEvent.ElementType.HELMET)
            return;

        ShaderHelper.renderFastRenderNotification();

        if(!overlayVisible)
            return;

        ShaderHelper.render(evt);

        // restore overlay for next render events
        Minecraft mc = Minecraft.getMinecraft();

        mc.getFramebuffer().bindFramebuffer(true);

        if(isOptifineSpecialCase)
            OptifineHelper.bindOptifineDepthBuffer();

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        mc.entityRenderer.setupOverlayRendering();
    }


}
