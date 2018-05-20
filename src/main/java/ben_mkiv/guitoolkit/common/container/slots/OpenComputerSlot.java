package ben_mkiv.guitoolkit.common.container.slots;

import ben_mkiv.ocUtils.CustomDriver;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.item.Container;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.common.Tier;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OpenComputerSlot extends filteredSlot {
    private int tier = Tier.One();
    private String type = Slot.Upgrade;
    public ArrayList<ItemStack> whitelistItems = new ArrayList<>();
    public ArrayList<ItemStack> blacklistItems = new ArrayList<>();

    public OpenComputerSlot(IInventory inventory, int index, int xpos, int ypos, int tier, String type) {
        super(inventory, index, xpos, ypos);
        this.tier = tier;
        this.type = type;
    }

    public OpenComputerSlot(IInventory inventory, int index, int xpos, int ypos, @Nonnull ItemStack container) {
        super(inventory, index, xpos, ypos);

        this.tier = Tier.None();
        this.type = li.cil.oc.api.driver.item.Slot.None;

        if(!container.isEmpty()) {
            DriverItem driver = CustomDriver.driverFor(container);
            //add slot provided by the itemStack
            if (driver != null && (driver instanceof Container)) {
                this.tier = ((Container) driver).providedTier(container);
                this.type = ((Container) driver).providedSlot(container);
            }
        }
    }

    @Override
    public void onSlotChanged(){
        super.onSlotChanged();
    }

    public String getSlotType(){
        return this.type;
    }

    public int getTier() {
        return this.tier;
    }

    @Override
    public boolean isItemValid(ItemStack stack){
        if(this.type.equals(Slot.None) || this.tier == Tier.None())
            return false;

        if(whitelistItems.size() > 0){
            boolean match = false;
            for(ItemStack i : whitelistItems)
                if(i.getItem().equals(stack.getItem()))
                    match = true;

            if(!match)
                return false;
        }

        if(blacklistItems.size() > 0){
            for(ItemStack i : blacklistItems)
                if(i.getItem().equals(stack.getItem()))
                    return false;
        }

        if(this.type.equals(Slot.Any) && this.tier == Tier.Any())
            return true;

        DriverItem drv = CustomDriver.driverFor(stack);
        if(drv == null)
            return false;

        if(!this.type.equals(drv.slot(stack)))
            return false;

        if(this.tier < drv.tier(stack))
            return false;

        return this.inventory.isItemValidForSlot(this.slotNumber, stack);
    }

    @SideOnly(Side.CLIENT)
    public boolean isEnabled(){
        return !type.equals(Slot.None) && tier != Tier.None();
    }


    @Override
    public List<String> getTooltip(List<String> tooltip){
        switch(getSlotType()){
            case "rack_mountable":
                tooltip.add("Type: Server Slot");
                break;
            case "upgrade":
                tooltip.add("Type: Upgrade Slot");
                tooltip.add("Tier: " + (getTier()+1));
                break;
        }

        return super.getTooltip(tooltip);
    }



}