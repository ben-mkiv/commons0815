package ben_mkiv.rendertoolkit.common.widgets.core.modifiers;

import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import ben_mkiv.rendertoolkit.common.widgets.core.Easing;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEasing;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;

public class WidgetModifierColor extends WidgetModifier implements IEasing {
	private float r, g, b, alpha;
	private float red, green, blue, Alpha;

	@Override
	public void addEasing(String type, String typeIO, float duration, String list, float min, float max, String mode){
		min = Math.max(0, Math.min(min, 1));
		max = Math.max(0, Math.min(max, 1));

		switch (list.toLowerCase()){
			case "red":
			case "r":
				super.addEasing(type, typeIO, duration, "r", min, max, mode);
				return;
			case "green":
			case "g":
				super.addEasing(type, typeIO, duration, "g", min, max, mode);
				return;
			case "blue":
			case "b":
				super.addEasing(type, typeIO, duration, "b", min, max, mode);
				return;
			case "alpha":
			case "a":
				super.addEasing(type, typeIO, duration, "a", min, max, mode);
				return;
		}
	}

	@Override
	public void removeEasing(String list){
		switch (list.toLowerCase()){
			case "red":
			case "r":
				super.removeEasing("r");
				return;
			case "green":
			case "g":
				super.removeEasing("g");
				return;
			case "blue":
			case "b":
				super.removeEasing("b");
				return;
			case "alpha":
			case "a":
				super.removeEasing("a");
				return;
		}
	}

	@Override
	public void update(float[] values){
		if(values.length < 3)
			return;

		this.r = Math.max(0, Math.min(values[0], 1));
		this.g = Math.max(0, Math.min(values[1], 1));
		this.b = Math.max(0, Math.min(values[2], 1));

		if(values.length >= 4)
			this.alpha = Math.max(0, Math.min(values[3], 1));
		else
			this.alpha = 1;

		this.applyEasings();
	}

	public WidgetModifierColor(float r, float g, float b, float alpha){
		this.setColor(r, g, b, alpha);
	}

	public void apply(long conditionStates){	
		if(!shouldApplyModifier(conditionStates)) return;

		this.applyEasings();

		if(this.Alpha < 1)
			GlStateManager.color(this.red, this.green, this.blue, this.Alpha);
		else
			GlStateManager.color(this.red, this.green, this.blue);
	}
	
	public void writeData(ByteBuf buff) {
		super.writeData(buff);
		buff.writeFloat(this.r);
		buff.writeFloat(this.g);
		buff.writeFloat(this.b);
		buff.writeFloat(this.alpha);
	}
	
	public void readData(ByteBuf buff) {
		super.readData(buff);
		this.setColor(buff.readFloat(), buff.readFloat(), buff.readFloat(), buff.readFloat());
	}

	private void setColor(float r, float g, float b, float alpha){
		this.r = Math.max(0, Math.min(r, 1));
		this.g = Math.max(0, Math.min(g, 1));
		this.b = Math.max(0, Math.min(b, 1));
		this.alpha = Math.max(0, Math.min(alpha, 1));
	}
	
	public WidgetModifierType getType(){
		return WidgetModifierType.COLOR;
	}

	private void applyEasings(){
		this.red   = Easing.applyEasing(easings.get("r"), this.r);
		this.green = Easing.applyEasing(easings.get("g"), this.g);
		this.blue  = Easing.applyEasing(easings.get("b"), this.b);
		this.Alpha = Easing.applyEasing(easings.get("a"), this.alpha);
	}

	public Object[] getValues(){
		this.applyEasings();
		return new Object[]{ this.red, this.green, this.blue, this.Alpha };
	}
}
