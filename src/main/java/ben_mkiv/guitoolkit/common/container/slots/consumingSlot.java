package ben_mkiv.guitoolkit.common.container.slots;

import ben_mkiv.inventoryUtils.ClientInventory;

public class consumingSlot extends filteredSlot {
    public consumingSlot(ClientInventory inventory, int index, int xPosition, int yPosition){
        super(inventory, index, xPosition, yPosition);
    }
}
