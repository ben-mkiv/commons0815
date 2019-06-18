package ben_mkiv.rendertoolkit.surface;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.*;

@SideOnly(Side.CLIENT)
public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();

	public final static Vec3d vec3d000 = new Vec3d(0, 0, 0);
	public final static Vector3f vec3f000 = new Vector3f(0, 0, 0);

	public int maxTrackingRange = 64;
	public static int viewDistance = 64;
	public boolean entityTrackerEnabled = true;

	public static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
	private Vec3d renderResolution = null;

	private WidgetCollection widgets = new WidgetCollection();

	public ClientSurface() {
	}

	public Vec3d getRenderResolution(UUID instanceUUID){
		return renderResolution;
	}

	public void setRenderResolution(Vec3d resolution, UUID instanceUUID){
		renderResolution = resolution;
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.getType() != ElementType.HELMET) return;
		if (!(evt instanceof RenderGameOverlayEvent.Post)) return;

		if(!shouldRenderStart(RenderType.GameOverlayLocated)) return;

		preRender(RenderType.GameOverlayLocated, evt.getPartialTicks());
		renderWidgets(widgets.getWidgetsOverlay().values());
		postRender(RenderType.GameOverlayLocated);
	}

	public static void renderSingleWidgetOverlay(IRenderableWidget widget, Long conditions){
		renderSingleWidgetOverlay(widget, conditions, vec3d000);
	}


	public static void renderSingleWidgetOverlay(IRenderableWidget widget, Long conditions, Vec3d location){
		preRender(RenderType.GameOverlayLocated, 0);
		widget.render(Minecraft.getMinecraft().player, location, conditions);
		postRender(RenderType.GameOverlayLocated);
	}

	public static void renderSingleWidgetWorld(IRenderableWidget widget, Long conditions, Vec3d location, float partialTicks){
		preRender(RenderType.WorldLocated, partialTicks);
		widget.render(Minecraft.getMinecraft().player, location, conditions);
		postRender(RenderType.WorldLocated);
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)	{
		if(!shouldRenderStart(RenderType.WorldLocated)) return;

		preRender(RenderType.WorldLocated, event.getPartialTicks());
		renderWidgets(widgets.getWidgetsWorld().values());
		postRender(RenderType.WorldLocated);
	}

	private void renderWidgets(Collection<IRenderableWidget> widgets){
		renderWidgets(widgets, Long.MAX_VALUE);
	}

	private void renderWidgets(Collection<IRenderableWidget> widgets, long renderConditions){
		String uuid = Minecraft.getMinecraft().player.getUniqueID().toString();


		for(IRenderableWidget renderable : widgets) {
			if(!renderable.shouldWidgetBeRendered(Minecraft.getMinecraft().player))
				continue;
			if(!renderable.isWidgetOwner(uuid))
				continue;

			renderWidget(renderable, renderConditions);
		}
	}

	public static void preRender(RenderType renderType, float partialTicks){
		GlStateManager.pushMatrix();

		switch (renderType){
			case WorldLocated:
				double[] playerLocation = getEntityPlayerLocation(Minecraft.getMinecraft().getRenderViewEntity(), partialTicks);
				GlStateManager.translate(-playerLocation[0], -playerLocation[1], -playerLocation[2]);
				GlStateManager.depthMask(true);
				break;

			case GameOverlayLocated:
				if(instances.getRenderResolution(null) != null)
					GlStateManager.scale(ClientSurface.resolution.getScaledWidth() / instances.getRenderResolution(null).x, ClientSurface.resolution.getScaledHeight() / instances.getRenderResolution(null).y, 1);

				GlStateManager.depthMask(true);
				break;
		}
	}

	public static void postRender(RenderType renderType){
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.disableDepth(); // this flag might depend on optifine
	}

	public static void renderWidget(IRenderableWidget widget, long conditionStates){
		renderWidget(widget, conditionStates, vec3d000);
	}

	public static void renderWidget(IRenderableWidget widget, long conditionStates, Vec3d renderOffset){
		GlStateManager.pushMatrix();
		widget.render(Minecraft.getMinecraft().player, renderOffset, conditionStates);
		GlStateManager.popMatrix();
	}

	public static double[] getEntityPlayerLocation(Entity e, float partialTicks){
		double x = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
		double y = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
		double z = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;
		return new double[]{x, y, z};
	}

	public static RayTraceResult getBlockCoordsLookingAt(EntityPlayer player){
		//RayTraceResult objectMouseOver = player.rayTrace(200, 1);
		if(Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK){
			return Minecraft.getMinecraft().objectMouseOver;
		}
		return null;
	}

	public boolean shouldRenderStart(RenderType renderEvent){
		return getWidgetCount(null, renderEvent) > 0;
	}

	public void updateWidgets(UUID uuid, Set<Map.Entry<Integer, Widget>> widgets){
		this.widgets.updateWidgets(widgets);
	}

	public void removeWidgets(UUID uuid, List<Integer> ids){
		widgets.removeWidgets(ids);
	}

	public void removeAllWidgets(UUID uuid){
		widgets.removeAllWidgets();
	}

	public int getWidgetCount(UUID uuid, RenderType renderEvent) {
		return widgets.getWidgetCount(renderEvent);
	}

	public void sendResolution(UUID instanceUUID){}
}
