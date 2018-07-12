package ben_mkiv.guitoolkit.common.container.slots;

import ben_mkiv.commons0815.utils.hotkeyEvent;
import net.minecraft.inventory.IInventory;

public class HotkeySyncSlot extends filteredSlot {
    public hotkeyEvent event;

    public HotkeySyncSlot(IInventory inventory, int inventorySlotIndex, int x, int y, int offsetY, hotkeyEvent event){
        super(inventory, inventorySlotIndex, x, y + offsetY);
        this.event = event;

        String tooltip = "";
        for(String group : event.group)
            tooltip+=" > " + group;

        tooltip+=" # " + event.action;
        customTooltip.add(tooltip.substring(3));
    }

    public void updatePositionX(int posX, int offsetX){
        this.xPos = posX - offsetX;
    }


}
