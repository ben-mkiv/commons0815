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
            return super.isItemValid(stack);

        for(ItemStack blocked : blacklist)
            if(ItemStack.areItemsEqual(stack, blocked))
                return false;

        for(ItemStack allowed : whitelist)
            if(ItemStack.areItemsEqual(stack, allowed))
                return super.isItemValid(stack);

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
