package ben_mkiv.rendertoolkit.common.widgets;

import ben_mkiv.rendertoolkit.common.widgets.core.Easing;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class WidgetModifier{
	public enum WidgetModifierType {
		TRANSLATE, COLOR, SCALE, ROTATE, TEXTURE, AUTOTRANSLATE
	}

	public HashMap<String, ArrayList> easings = new HashMap<>();

	public long conditions = 0;

	public boolean shouldApplyModifier(long conditionStates){
		if((this.conditions & conditionStates) == this.conditions) 
			return true;
			
		return false;
	}
		
	public Object[] getConditions(){
		Object[] foo = new Object[64];
				
		for(short i=0, s=0; i < 64; i++){
			if(((conditions >>> i) & (long) 1) != 0){
				foo[s] = WidgetModifierConditionType.getName(i);
				s++;
		} }
					
		return foo;
	}	
	
	public void writeData(ByteBuf buff){
		buff.writeLong(this.conditions);

		buff.writeInt(easings.size());
		for(Map.Entry<String, ArrayList> entry : easings.entrySet()) {
			ByteBufUtils.writeUTF8String(buff, entry.getKey());
			Easing.writeEasing(buff, entry.getValue());
		}
	}
	
	public void readData(ByteBuf buff){
		this.conditions = buff.readLong();

		int easingCount = buff.readInt();
		for(int i=0; i < easingCount; i++){
			String listName = ByteBufUtils.readUTF8String(buff);
			easings.remove(listName);
			easings.put(listName, Easing.readEasing(buff));
		}
	}
	
	public void configureCondition(short type, boolean state){		
		if(state == true) 
			this.conditions |= ((long) 1 << type); 
		else
			this.conditions &= ~((long) 1 << type); 
	}

	public void addEasing(String type, String typeIO, float duration, String list, float min, float max, String mode){
		removeEasing(list.toLowerCase());
		easings.put(list.toLowerCase(), Easing.setEasing(Easing.EasingType.valueOf(type.toUpperCase()), Easing.EasingTypeIO.valueOf(typeIO.toUpperCase()), duration, min, max, Easing.EasingTypeMode.valueOf(mode.toUpperCase())));
	}

	public void removeEasing(String list){
		easings.remove(list.toLowerCase());
	}

	public HashMap<String, ArrayList> getEasings(){ return easings; }
	
	//this stuff should be overwritten by childs
	public void apply(long conditionStates){}
	public void revoke(long conditionStates){}
	public void update(float[] values){}
	public WidgetModifierType getType(){ return null; }
	public Object[] getValues(){ return null; }
}
