package ben_mkiv.rendertoolkit.common.widgets.core.modifiers;

import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import ben_mkiv.rendertoolkit.common.widgets.core.Easing;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEasing;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;

public class WidgetModifierRotate extends WidgetModifier implements IEasing {
	private float deg, x, y, z;
	private float DEG;
	private float X;
	public float Y;
	private float Z;

	@Override
	public void update(float[] values){
		if(values.length < 4)
			return;

		this.deg = values[0];
		this.x = values[1];
		this.y = values[2];
		this.z = values[3];

		this.applyEasings();
	}

	public WidgetModifierRotate(float deg, float x, float y, float z){
		this.deg = deg;
		this.x = x;
		this.y = y;
		this.z = z;
	}
		
	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;

		this.applyEasings();


		GlStateManager.rotate(DEG, X, Y, Z);
	}

	public void revoke(long conditionStates){
		if(!shouldApplyModifier(conditionStates)) return;
		GlStateManager.rotate(-DEG, X, Y, Z);
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(this.deg);
		buff.writeFloat(this.x);
		buff.writeFloat(this.y);
		buff.writeFloat(this.z);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.deg = buff.readFloat();
		this.x = buff.readFloat();
		this.y = buff.readFloat();
		this.z = buff.readFloat();
	}
	
	public WidgetModifierType getType(){
		return WidgetModifierType.ROTATE;
	}	

	private void applyEasings(){
		this.X = Easing.applyEasing(easings.get("x"), this.x);
		this.Y = Easing.applyEasing(easings.get("y"), this.y);
		this.Z = Easing.applyEasing(easings.get("z"), this.z);
		this.DEG = Easing.applyEasing(easings.get("deg"), this.deg);
	}

	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.deg, this.x, this.y, this.z };
	}
}
