package ben_mkiv.inventoryUtils;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;

public class ConsumingInventory extends ClientInventory {
    HashMap<Integer, ArrayList<ItemStack>> whitelists = new HashMap<>();
    HashMap<Integer, ArrayList<ItemStack>> blacklists = new HashMap<>();

    public ConsumingInventory(int size, Container container){
        super(size, container);
        setSize(size);
    }

    public void addBlacklistItem(int slot, ItemStack item){
        if(blacklists.get(slot) == null)
            blacklists.put(slot, new ArrayList<>());

        blacklists.get(slot).add(item);
    }

    public void addWhitelistItem(int slot, ItemStack item){
        if(whitelists.get(slot) == null)
            whitelists.put(slot, new ArrayList<>());

        whitelists.get(slot).add(item);
    }

    public ArrayList<ItemStack> getWhitelist(int slot){
        if(whitelists.get(slot) != null)
            return whitelists.get(slot);
        else
            return new ArrayList<>();
    }

    public ArrayList<ItemStack> getBlacklist(int slot){
        if(blacklists.get(slot) != null)
            return blacklists.get(slot);
        else
            return new ArrayList<>();
    }

    //no idea why, but we have to override this, even if it does the same...
    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot){
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }
}
