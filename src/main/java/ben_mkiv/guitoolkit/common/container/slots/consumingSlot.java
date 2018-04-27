package ben_mkiv.guitoolkit.common.container.slots;

import ben_mkiv.inventoryUtils.ConsumingInventory;

public class consumingSlot extends filteredSlot {
    public consumingSlot(ConsumingInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
    }

    private ConsumingInventory getInventory(){
        return (ConsumingInventory) this.inventory;
    }

    @Override
    public void onSlotChanged(){
        super.onSlotChanged();
        getInventory().onSlotChanged(getSlotIndex());
    }
}
