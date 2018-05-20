package ben_mkiv.guitoolkit.common.container.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class filteredSlot extends customSlot {
    public ArrayList<ItemStack> whitelist = new ArrayList<>();
    public ArrayList<ItemStack> blacklist = new ArrayList<>();

    public filteredSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if(blacklist.size() == 0 && whitelist.size() == 0)
            return true;

        for(ItemStack blocked : blacklist)
            if(ItemStack.areItemsEqual(new ItemStack(stack.getItem(), blocked.getCount(), stack.getMetadata()), blocked))
                return false;

        for(ItemStack allowed : whitelist)
            if(ItemStack.areItemsEqual(new ItemStack(stack.getItem(), allowed.getCount(), stack.getMetadata()), allowed))
                return true;

        return false;
    }

    @Override
    public List<String> getTooltip(List<String> tooltip){
        if(whitelist.size() > 0) {
            tooltip.add(TextFormatting.GREEN + "accepts: ");
            for(ItemStack item : whitelist) {
                tooltip.add(item.getDisplayName());
            }
        }
        if(blacklist.size() > 0) {
            tooltip.add(TextFormatting.RED + "blocked: ");
            for(ItemStack item : blacklist)
                tooltip.add(item.getDisplayName());
        }

        return super.getTooltip(tooltip);
    }

}
