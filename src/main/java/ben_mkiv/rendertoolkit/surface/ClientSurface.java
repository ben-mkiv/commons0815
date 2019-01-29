package ben_mkiv.rendertoolkit.surface;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@SideOnly(Side.CLIENT)
public class ClientSurface {
	public static ClientSurface instances = new ClientSurface();
	public Map<Integer, IRenderableWidget> renderables = new ConcurrentHashMap<>();
	public Map<Integer, IRenderableWidget> renderablesWorld = new ConcurrentHashMap<>();

	public int maxTrackingRange = 64;
	public static int viewDistance = 64;
	public boolean entityTrackerEnabled = true;

	public static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
	public static Vec3d renderResolution = null;

	public ClientSurface() {
	}


	//gets the current widgets and puts them to the correct hashmap
	public void updateWidgets(Set<Entry<Integer, Widget>> widgets){
		for(Entry<Integer, Widget> widget : widgets){
			IRenderableWidget r = widget.getValue().getRenderable();
			switch(r.getRenderType()){
				case GameOverlayLocated:
					renderables.put(widget.getKey(), r);
					break;
				case WorldLocated:
					renderablesWorld.put(widget.getKey(), r);
					break;
			}
		}
	}

	public int getWidgetCount(){
		return (renderables.size() + renderablesWorld.size());
	}

	public void removeWidgets(List<Integer> ids){
		for(Integer id : ids){
			renderables.remove(id);
			renderablesWorld.remove(id);
		}
	}
	
	public void removeAllWidgets(){
		renderables.clear();
		renderablesWorld.clear();
	}


	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.getType() != ElementType.HELMET) return;
		if (!(evt instanceof RenderGameOverlayEvent.Post)) return;

		if(!shouldRenderStart(RenderType.GameOverlayLocated)) return;

		preRender(RenderType.GameOverlayLocated, evt.getPartialTicks());
		renderWidgets(renderables.values());
		postRender(RenderType.GameOverlayLocated);
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
		renderWidgets(renderablesWorld.values());
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
				double[] playerLocation = getEntityPlayerLocation(Minecraft.getMinecraft().player, partialTicks);
				GlStateManager.translate(-playerLocation[0], -playerLocation[1], -playerLocation[2]);
				GlStateManager.depthMask(true);
				break;

			case GameOverlayLocated:
				if(renderResolution != null)
					GlStateManager.scale(ClientSurface.resolution.getScaledWidth() / renderResolution.x, ClientSurface.resolution.getScaledHeight() / renderResolution.y, 1);

				GlStateManager.depthMask(true);
				break;
		}
	}

	public static void postRender(RenderType renderType){
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
	}

	public static void renderWidget(IRenderableWidget widget, long conditionStates){
		renderWidget(widget, conditionStates, new Vec3d(0, 0, 0));
	}

	public static void renderWidget(IRenderableWidget widget, long conditionStates, Vec3d location){
		GlStateManager.pushMatrix();
		widget.render(Minecraft.getMinecraft().player, location, conditionStates);
		GlStateManager.popMatrix();
	}

	public boolean shouldRenderStart(RenderType renderEvent){
		switch(renderEvent){
			case GameOverlayLocated:
				if(renderables.size() < 1) return false;

				break;

			case WorldLocated:
				if(renderablesWorld.size() < 1) return false;

				break;

		}

		return true;
	}

	public static double[] getEntityPlayerLocation(EntityPlayer e, float partialTicks){
		double x = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
		double y = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
		double z = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;
		return new double[]{x, y, z};
	}

	public static RayTraceResult getBlockCoordsLookingAt(EntityPlayer player){
		RayTraceResult objectMouseOver = player.rayTrace(200, 1);
		if(objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK){
			return objectMouseOver;
		}
		return null;
	}

}
