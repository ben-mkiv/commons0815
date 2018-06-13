package ben_mkiv.openentity.common.container.slots;

import ben_mkiv.guitoolkit.common.container.slots.filteredSlot;
import ben_mkiv.openentity.common.capability.hotkeys.hotkeyEvent;
import net.minecraft.inventory.IInventory;


public class HotkeySyncSlot extends filteredSlot {
    public hotkeyEvent event;

    public HotkeySyncSlot(IInventory customInventory, int slot, int x, int y, int offsetY, hotkeyEvent event, String label){
        super(customInventory, slot, x, y - 8);
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
