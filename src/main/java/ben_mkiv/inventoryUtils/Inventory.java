package ben_mkiv.inventoryUtils;

import ben_mkiv.commons0815.utils.ItemUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

//TODO: reimplement checkInventorySpace()
public abstract class Inventory extends ItemStackHandler implements IItemHandler, IInventory{
    public boolean ignoreNullStacks(){
        return false;
    }

    @Override
    public void clear(){
        this.markDirty();
    }

    @Override
    public int getSizeInventory(){
        return this.stacks.size();
    }

    @Override
    public boolean hasCustomName(){
        return true;
    }

    @Override
    public void closeInventory(EntityPlayer player){}

    @Override
    public String getName(){
        return "Inventory."+getClass().getSimpleName();
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value){}

    @Override
    public boolean isUsableByPlayer(EntityPlayer player){ return true; }

    @Override
    public ITextComponent getDisplayName(){
        return new TextComponentString("Inventory.java.DisplayName");
    }

    @Override
    public ItemStack removeStackFromSlot(int slot){
        return extractItem(slot, getStackInSlot(slot).getCount(), false);
    }

    @Override
    public void openInventory(EntityPlayer player){}

    @Override
    public boolean isEmpty(){
        for(int i=0; i < getSizeInventory(); i++)
            if(!getStackInSlot(i).isEmpty())
                return false;

        return true;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slot, int number) {
        return extractItem(slot, number, false);
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        setStackInSlot(slot, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    public boolean updateSize(int slotCount, Entity entity){
        boolean sizeChanged = (slotCount != this.stacks.size());

        if(!sizeChanged)
            return false;

        if(slotCount < this.stacks.size() && !entity.world.isRemote){
            ArrayList<ItemStack> dropItems = new ArrayList<>();
            for(int i = slotCount;  i < this.getSizeInventory(); i++)
                dropItems.add(getStackInSlot(i).copy());

            ItemUtil.dropItemList(dropItems, entity.world, entity.getPosition(), false);
        }

        ArrayList<ItemStack> oldItems = new ArrayList<>();
        for(int i = 0;  i < slotCount && i < getSizeInventory(); i++)
            oldItems.add(getStackInSlot(i).copy());

        this.setSize(slotCount);

        for(int i=0; i < oldItems.size() && i < slotCount; i++)
            this.setStackInSlot(i, oldItems.get(i));

        return true;
    }

}