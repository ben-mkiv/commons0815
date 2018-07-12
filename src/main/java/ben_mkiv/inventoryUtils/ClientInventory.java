package ben_mkiv.inventoryUtils;

import ben_mkiv.guitoolkit.common.container.IContentChangedListener;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ClientInventory extends Inventory implements IContentChangedListener {
    Container container;

    public ClientInventory(int size, Container container){
        setSize(size);
        this.container = container;
    }

    @Override
    public void onContentsChanged(int slot){
        super.onContentsChanged(slot);

        if(container instanceof IContentChangedListener)
            ((IContentChangedListener) container).onContentsChanged(slot);
    }

    //no idea why, but we have to override this, even if it does the same...
    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot){
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }
}

