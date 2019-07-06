package ben_mkiv.rendertoolkit.client.thermalvision;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;

public class ThermalEntityRender {

    ThermalEntityRender(){
        VazkiiShaderHelper.initShaders();
    }

    private static double currentDistance = 0;
    private static boolean isDead = false;

    static HashSet<Entity> forceGlowingEntities = new HashSet<>();

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
        // handle glowing state
        if(!event.getEntity().isGlowing()) {
            forceGlowingEntities.add(event.getEntity());
            event.getEntity().setGlowing(true);
        }

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
        currentDistance = event.getEntity().getPositionVector().distanceTo(Minecraft.getMinecraft().player.getPositionVector());

        // reenable depth testing (which gets disabled in outline renderer)
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // set thermalcolor rendershader
        VazkiiShaderHelper.useShader(VazkiiShaderHelper.thermalColorShader, callback);
    }

    @SubscribeEvent
    public void postRender(RenderLivingEvent.Post<EntityLivingBase> event){
        VazkiiShaderHelper.releaseShader();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderFogEvent(EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.05f);
        GlStateManager.setFog(GlStateManager.FogMode.EXP2);
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderFogEvent(EntityViewRenderEvent.FogColors event) {
        event.setRed(0.2f);
        event.setGreen(0.4f);
        event.setBlue(0.85f);
    }
}
