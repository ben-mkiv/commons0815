package ben_mkiv.rendertoolkit.client.renderer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ben_mkiv.rendertoolkit.client.event.ClientEventHandler;
import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.Widget;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRenderer {
	public static ClientEventHandler eventHandler;
	public Map<Integer, IRenderableWidget> renderables = new ConcurrentHashMap<Integer, IRenderableWidget>();
	public Map<Integer, IRenderableWidget> renderablesWorld = new ConcurrentHashMap<Integer, IRenderableWidget>();

	public static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
	public static Vec3d renderResolution = null;

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

	public boolean shouldWidgetBeRendered(EntityPlayer player, IRenderableWidget widget){
		return widget.shouldWidgetBeRendered(player);
	}

	public void preRender(EntityPlayer player){

	}

	public long getConditionStates(EntityPlayer player){
		return 0;
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent evt) {
		if (evt.getType() != ElementType.HELMET) return;
		if (!(evt instanceof RenderGameOverlayEvent.Post)) return;

		if(!shouldRenderStart(true)) return;
		if(renderables.size() < 1) return;

		EntityPlayer player = Minecraft.getMinecraft().player;

		this.preRender(player);

		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();
		GL11.glDepthMask(false);

		if(renderResolution != null) {
			GL11.glScaled(ClientRenderer.resolution.getScaledWidth() / renderResolution.x, ClientRenderer.resolution.getScaledHeight() / renderResolution.y, 1);
		}
		for(IRenderableWidget renderable : renderables.values()){
			if(shouldWidgetBeRendered(player, renderable)){
				GL11.glPushMatrix();
				renderable.render(player, getRenderOffset(), getConditionStates(player));
				GL11.glPopMatrix();
			}			
		}
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}
	
	public boolean shouldRenderStart(boolean renderingOverlay){
		return renderingOverlay;
	}		
	
	public double[] getEntityPlayerLocation(EntityPlayer e, float partialTicks){
		double x = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
		double y = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
		double z = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;
		return new double[]{x, y, z};
	}

	public Vec3d getRenderOffset(){
		return new Vec3d(0, 0, 0);
	}

	
	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)	{	
		if(renderablesWorld.size() < 1) return;		
		if(!shouldRenderStart(false)) return;

		EntityPlayer player = Minecraft.getMinecraft().player;

		double[] playerLocation = getEntityPlayerLocation(player, event.getPartialTicks());

		preRender(player);
		
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();
		
		GL11.glTranslated(-playerLocation[0], -playerLocation[1], -playerLocation[2]);
		GL11.glTranslated(getRenderOffset().x, getRenderOffset().y, getRenderOffset().z);
		
		GL11.glDepthMask(true);
		//Start Drawing In World		
		for(IRenderableWidget renderable : renderablesWorld.values()){
			if(shouldWidgetBeRendered(player, renderable)){
				GL11.glPushMatrix();
				renderable.render(player, getRenderOffset(), getConditionStates(player));
				GL11.glPopMatrix();
		} }		
		//Stop Drawing In World
		GL11.glPopMatrix();		
		GL11.glPopAttrib();
	}
	


}
