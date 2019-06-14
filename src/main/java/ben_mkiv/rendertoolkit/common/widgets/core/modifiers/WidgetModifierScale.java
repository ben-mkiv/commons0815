package ben_mkiv.rendertoolkit.common.widgets.core.modifiers;

import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import ben_mkiv.rendertoolkit.common.widgets.core.Easing;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEasing;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;

public class WidgetModifierScale extends WidgetModifier implements IEasing {
	private float x=1, y=1, z=1;
	private float X, Y, Z;

	public WidgetModifierScale(float x, float y, float z){
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

		GlStateManager.scale(X, Y, Z);
	}

	private void applyEasings(){
		this.X = Easing.applyEasing(easings.get("x"), this.x);
		this.Y = Easing.applyEasing(easings.get("y"), this.y);
		this.Z = Easing.applyEasing(easings.get("z"), this.z);
	}

	public void revoke(long conditionStates) {
		if (!shouldApplyModifier(conditionStates)) return;

		if(X > 0) GlStateManager.scale(1/X, 1, 1);
		if(Y > 0) GlStateManager.scale(1, 1/Y, 1);
		if(Z > 0) GlStateManager.scale(1, 1, 1/Z);
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
		return WidgetModifierType.SCALE;
	}
	
	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.X, this.Y, this.Z };
	}
}
