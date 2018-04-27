package ben_mkiv.guitoolkit.common.container.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class customSlot extends Slot {
    public boolean visible = true;

    public customSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isEnabled(){
        return this.visible;
    }
}
