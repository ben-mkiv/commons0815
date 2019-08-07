package ben_mkiv.rendertoolkit.common.widgets;

import ben_mkiv.commons0815.utils.utilsCommon;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IPrivate;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IResizable;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.UUID;

import static ben_mkiv.rendertoolkit.surface.ClientSurface.vec3f000;

public abstract class WidgetGLOverlay extends Widget implements IResizable, IPrivate {
	public RenderType rendertype = RenderType.GameOverlayLocated;

	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f margin = new Vector3f(0, 0, 0);

	public float width = 1, height = 1;

	public enum VAlignment{	TOP, MIDDLE, BOTTOM }
	public enum HAlignment{	LEFT, CENTER, RIGHT }

	protected VAlignment valign = VAlignment.BOTTOM;
	protected HAlignment halign = HAlignment.RIGHT;

	boolean isThroughVisibility = false;
	private boolean isLookingAtEnable = false;

	public boolean faceWidgetToPlayer = false;

	private Vec3d lookAt = new Vec3d(0, 0, 0);

	private int viewDistance = 64;

	private long age = 0;

	protected WidgetGLOverlay(){}
	
	public void writeData(ByteBuf buff) {
		WidgetModifierList.writeData(buff);
		buff.writeInt(viewDistance);
		buff.writeDouble(lookAt.x);
		buff.writeDouble(lookAt.y);
		buff.writeDouble(lookAt.z);
		buff.writeFloat(pos.x);
		buff.writeFloat(pos.y);
		buff.writeFloat(pos.z);
		buff.writeInt(valign.ordinal());
		buff.writeInt(halign.ordinal());
		buff.writeBoolean(isLookingAtEnable);
	}
	
	public void readData(ByteBuf buff) {
		WidgetModifierList.readData(buff);
		viewDistance = buff.readInt();
		lookAt = new Vec3d(buff.readDouble(), buff.readDouble(), buff.readDouble());
		pos = new Vector3f(buff.readFloat(), buff.readFloat(), buff.readFloat());
		valign = VAlignment.values()[buff.readInt()];
		halign = HAlignment.values()[buff.readInt()];
		isLookingAtEnable = buff.readBoolean();
	}

	protected void writeDataSIZE(ByteBuf buff) {
		buff.writeFloat(this.width);
		buff.writeFloat(this.height);
	}

	protected void readDataSIZE(ByteBuf buff) {
		this.width = buff.readFloat();
		this.height = buff.readFloat();
	}
	
	public void setSize(double w, double h) {
		this.width = (float) w;
		this.height = (float) h;
	}

	public Vector3f getPosition(){
		return this.pos;
	}
	
	public double getWidth() {
		return this.width; }

	public double getHeight() {
		return this.height; }
	
	public int getDistanceView() {
		return viewDistance; }

	public void setDistanceView(int distance) {
		this.viewDistance = distance; }

	public void setLookingAt(double x, double y, double z) {
		lookAt = new Vec3d(x, y, z); }

	public boolean isLookingAtEnable() {
		return isLookingAtEnable; }

	public void setLookingAtEnable(boolean enable) {
		isLookingAtEnable = enable; }
		
	public void setFaceWidgetToPlayer(boolean enable) {
		faceWidgetToPlayer = enable; }

	public double getLookingAtX() {
		return lookAt.x; }

	public double getLookingAtY() {
		return lookAt.y; }

	public double getLookingAtZ() {
		return lookAt.z; }

	public void setVerticalAlignment(String align){
		this.valign = VAlignment.valueOf(align.toUpperCase());
	}

	public void setHorizontalAlignment(String align){
		this.halign = HAlignment.valueOf(align.toUpperCase());
	}

	@SideOnly(Side.CLIENT)
	public abstract class RenderableGLWidget implements IRenderableWidget {
		boolean doBlending, doTexture, doSmoothShade, doAlpha;

		void setRenderFlags() {
			doBlending = false;
			doTexture = false;
			doSmoothShade = false;

			if(isThroughVisibility)
				GlStateManager.disableDepth();
			else
				GlStateManager.enableDepth();

			GlStateManager.disableLighting();

			for(WidgetModifier modifier : WidgetModifierList.modifiers){
				switch(modifier.getType()){
					case COLOR:
						if((float) modifier.getValues()[3] == 1)
							continue;
						doAlpha = true;
						doBlending = true;
						GlStateManager.enableDepth();
						GlStateManager.depthMask(true);
						continue;
					case TEXTURE:
						doTexture = true;
						continue;
				}
			}

			switch(getType()){
				case BOX2D:
					doAlpha = true;
					doBlending = true;
					doSmoothShade = true;
					//doTexture = false;
					break;

				case TEXT2D:
				case TEXT3D:
					doAlpha = true;
					doBlending = true;
					doTexture = true;
					break;

				case FLUID2D:
				case ENTITY2D:
				case OCSCREEN2D:
				case OCSCREEN3D:
				case ITEM2D:
					GlStateManager.enableDepth();
					GlStateManager.depthMask(true);
				case ITEM3D:
					doAlpha = true;
					doBlending = true;
					doTexture = true;
					break;
			}

			if(doTexture)
				GlStateManager.enableTexture2D();
			else
				GlStateManager.disableTexture2D();

			if(doBlending){
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			else {
				GlStateManager.disableBlend();
			}

			GlStateManager.shadeModel(doSmoothShade ? GL11.GL_SMOOTH : GL11.GL_FLAT);

			if(doAlpha)
				GlStateManager.enableAlpha();
			else
				GlStateManager.disableAlpha();
		}


		public int preRender(long conditionStates){
			age++;
			this.setRenderFlags();
			if(age % 50 == 0) updateRenderPosition(conditionStates);
			return WidgetModifierList.getCurrentColor(conditionStates, 0);
		}

		void updateRenderPosition(long conditionStates){
			updateRenderPosition(conditionStates, new Vec3d(0, 0, 0));
		}

		void updateRenderPosition(long conditionStates, Vec3d renderOrigin){
			Vec3d renderPosition = WidgetModifierList.getRenderPosition(conditionStates, renderOrigin, ClientSurface.resolution.getScaledWidth(), ClientSurface.resolution.getScaledHeight(), 1);
			pos = new Vector3f((float) renderPosition.x, (float) renderPosition.y, (float) renderPosition.z);
			pos.add(margin);
		}

		public int applyModifiers(long conditionStates){
			WidgetModifierList.apply(conditionStates);

			return WidgetModifierList.getCurrentColor(WidgetModifierList.lastConditionStates, 0);
		}

		protected void addPlayerRotation(EntityPlayer player){
			if(!faceWidgetToPlayer) return;

			GL11.glRotated(player.rotationYaw,0.0D,1.0D,0.0D);
			GL11.glRotated(-player.rotationPitch,1.0D,0.0D,0.0D);
		}

		public void removePlayerRotation(EntityPlayer player){
			if(!faceWidgetToPlayer) return;

			GL11.glRotated(player.rotationPitch,1.0D,0.0D,0.0D);
			GL11.glRotated(-player.rotationYaw,0.0D,1.0D,0.0D);
		}

		public void addPlayerRotation(EntityPlayer player, Vec3d lookingAtVector){
			if(!faceWidgetToPlayer) return;

			GL11.glRotated(player.rotationYaw,0.0D,1.0D,0.0D);

			if(lookingAtVector.y > (player.getPositionVector().y + 1.8F))
				GL11.glRotated(-player.rotationPitch,1.0D,0.0D,0.0D);
			else
				GL11.glRotated(player.rotationPitch,1.0D,0.0D,0.0D);
		}

		public void removePlayerRotation(EntityPlayer player, Vec3d lookingAtVector){
			if(!faceWidgetToPlayer) return;

			if(lookingAtVector.y > (player.getPositionVector().y + 1.8F))
				GL11.glRotated(player.rotationPitch,1.0D,0.0D,0.0D);
			else
				GL11.glRotated(-player.rotationPitch,1.0D,0.0D,0.0D);

			GL11.glRotated(-player.rotationYaw,0.0D,1.0D,0.0D);
		}

		protected void applyModifierList(long conditionStates, ArrayList<WidgetModifier.WidgetModifierType> modifierTypes){
			for(WidgetModifier m : WidgetModifierList.modifiers){
				for(WidgetModifier.WidgetModifierType t : modifierTypes){
					if(t.equals(m.getType())){
						m.apply(conditionStates);
					}
				}
			}
		}

		public void revokeModifierList(ArrayList<WidgetModifier.WidgetModifierType> modifierTypes){
			WidgetModifierList.revoke(WidgetModifierList.lastConditionStates, modifierTypes);
		}

		public void revokeModifiers() {
			WidgetModifierList.revoke(WidgetModifierList.lastConditionStates);
		}

		protected float[] getCurrentColorFloat(long conditionStates, int index){
			return WidgetModifierList.getCurrentColorFloat(conditionStates, index);
		}

		public void postRender(){}


		@Override
		public boolean shouldWidgetBeRendered(EntityPlayer player) {
			return shouldWidgetBeRendered(player, new Vector3f(0, 0, 0));
		}

		@Override
		public boolean shouldWidgetBeRendered(EntityPlayer player, Vector3f offset) {
			if(getRenderType().equals(RenderType.WorldLocated)) {
				if(offset.equals(vec3f000))
					return true;

				offset.add(pos);
				if(Minecraft.getMinecraft().player.getPositionVector().distanceTo(new Vec3d(offset.x, offset.y, offset.z)) > viewDistance)
					return false;
			}

			RayTraceResult pos = ClientSurface.getBlockCoordsLookingAt(player);
			if(isLookingAtEnable && (pos == null || pos.getBlockPos().getX() != lookAt.x || pos.getBlockPos().getY() != lookAt.y || pos.getBlockPos().getZ() != lookAt.z) )
				return false;		
					
			return isVisible();
		}

		@Override
		public UUID getWidgetOwner() {
			return getOwnerUUID();
		}

		@Override
		public RenderType getRenderType() {
			return rendertype;
		}

		protected VAlignment getVerticalAlign(){
			return valign;
		}

		protected HAlignment getHorizontalAlign(){
			return halign;
		}

		public boolean isVisible(){
			return isVisible;
		}

		public boolean isLookingAtEnabled(){
			return isLookingAtEnable;
		}

		public Vec3d lookingAtVector(){
			return lookAt;
		}
	}
}
