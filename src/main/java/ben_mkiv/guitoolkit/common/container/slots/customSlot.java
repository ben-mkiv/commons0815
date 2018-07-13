package ben_mkiv.guitoolkit.common.container.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class customSlot extends Slot {
    public boolean visible = true;
    public ArrayList<String> customTooltip = new ArrayList<>();
    public String languageKey = "";

    public customSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isEnabled(){
        return this.visible;
    }

    public List<String> getTooltip(List<String> tooltip){
        tooltip.addAll(customTooltip);
        return tooltip;
    }

    public void setLanguageKey(String key){
        languageKey = key;
    }
}
