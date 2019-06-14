package ben_mkiv.rendertoolkit.common.widgets.core.modifiers;

import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import ben_mkiv.rendertoolkit.common.widgets.core.Easing;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEasing;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;

public class WidgetModifierTranslate extends WidgetModifier implements IEasing {
	public float x, y, z;
	public float renderX, renderY, renderZ;

	public WidgetModifierTranslate(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void update(float[] values){
		if(values.length < 3)
			return;

		this.x = values[0];
		this.y = values[1];
		this.z = values[2];

		this.applyEasings();
	}
		
	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;

		this.applyEasings();

		GlStateManager.translate(renderX, renderY, renderZ);
	}

	public void applyEasings(){
		this.renderX = Easing.applyEasing(easings.get("x"), this.x);
		this.renderY = Easing.applyEasing(easings.get("y"), this.y);
		this.renderZ = Easing.applyEasing(easings.get("z"), this.z);
	}

	public void revoke(long conditionStates) {
		if (!shouldApplyModifier(conditionStates)) return;
		GlStateManager.translate(-renderX, -renderY, -renderZ);
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(this.x);
		buff.writeFloat(this.y);
		buff.writeFloat(this.z);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.x = buff.readFloat();
		this.y = buff.readFloat();
		this.z = buff.readFloat();
	}	
	
	public WidgetModifierType getType(){
		return WidgetModifierType.TRANSLATE;
	}
	
	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.renderX, this.renderY, this.renderZ};
	}

}
