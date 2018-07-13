package ben_mkiv.inventoryUtils;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ClientInventory extends Inventory {
    public Container container;

    public ClientInventory(int size, Container container){
        setSize(size);
        this.container = container;
    }

    /*
    @Override
    public void onContentsChanged(int slot){

        for(Slot invSlot : container.inventorySlots)
            if(invSlot.inventory.equals(this) && invSlot.getSlotIndex() == slot)
                if(invSlot instanceof customSlot)
                    invSlot.onSlotChanged();

        super.onContentsChanged(slot);
    }*/

    //no idea why, but we have to override this, even if it does the same...
    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot){
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }
}

