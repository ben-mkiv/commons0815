package ben_mkiv.commons0815.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ItemUtil {

    public static ItemStack getOCItemStack(String name){
        try {
            ItemStack stack = li.cil.oc.api.Items.get(name.toLowerCase()).createItemStack(1);
            return stack;
        } catch (Exception e){}

        return ItemStack.EMPTY;
    }

    public static void dropInventory(IInventory inventory, Entity entity){
        if(entity == null)
            return;

        dropInventory(inventory, entity.world, entity.getPosition());
    }

    public static void dropInventory(IInventory inventory, World world, BlockPos pos){
        if(inventory == null)
            return;

        ArrayList<ItemStack> drop = new ArrayList<>();

        for(int i=0; i < inventory.getSizeInventory(); i++)
            if(!inventory.getStackInSlot(i).isEmpty())
                drop.add(inventory.getStackInSlot(i));

        ItemUtil.dropItemList(drop, world, pos, true);
    }

    public static void dropItemList(ArrayList<ItemStack> items, World world, BlockPos pos, boolean motion){
        for(ItemStack stack: items)
            dropItem(stack, world, pos, motion);
    }

    public static void dropItemList(NonNullList<ItemStack> items, World world, BlockPos pos, boolean motion){
        for(ItemStack stack: items)
            dropItem(stack, world, pos, motion);
    }

    public static void dropItem(ItemStack stack, World world, BlockPos pos, boolean motion) {
        dropItem(stack, world, pos, motion, 10);
    }

    public static void dropItem(ItemStack stack, World world, BlockPos pos, boolean motion, int pickupDelay){
        if(world.isRemote) return;

        if(stack.getMaxStackSize() <= 0 || stack.isEmpty())
            return;

        EntityItem entityitem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        entityitem.setPickupDelay(pickupDelay);
        if(!motion){
            entityitem.motionX=0;
            entityitem.motionY=0;
            entityitem.motionZ=0;
        }

        world.spawnEntity(entityitem);
    }

    public static ItemStack suckItems(World world, int x, int y, int z, @Nonnull ItemStack filter, int num){
        if(world.isRemote)
            return ItemStack.EMPTY;

        AxisAlignedBB box = new AxisAlignedBB(1,1,1,16,16,16);

        box.offset(x-1, y-1, z-1);
        for(Entity ent : world.getEntitiesWithinAABB(EntityItem.class, box)){
            if(!(ent instanceof EntityItem)) continue;
            ItemStack stack = ((EntityItem)ent).getItem();
            if(!filter.equals(ItemStack.EMPTY) && filter.isItemEqual(stack)) continue;
            ItemStack ret = stack.copy();
            ret.setCount(Math.min(ret.getMaxStackSize(), num));
            stack.shrink(ret.getMaxStackSize());
            if(stack.getMaxStackSize()<1)ent.setDead();
            return ret;
        }

        return ItemStack.EMPTY;
    }

    public static boolean hasDroppedItems(World world, int x, int y, int z){
        if(world.isRemote)
            return false;

        AxisAlignedBB box = new AxisAlignedBB(1,1,1,16,16,16);
        box.offset(x-1, y-1, z-1);

        return !world.getEntitiesWithinAABB(EntityItem.class, box).isEmpty();
    }

    public static ItemStack sumItemStacks(ItemStack stackA, ItemStack stackB, boolean pull){
        if(stackA == null || stackB == null)
            return ItemStack.EMPTY;

        if(!stackA.isItemEqual(stackB))
            return ItemStack.EMPTY;

        ItemStack res = stackA.copy();

        int size = stackA.getMaxStackSize() + stackB.getMaxStackSize();
        size = Math.min(size, res.getMaxStackSize());
        res.setCount(size);
        if(pull){
            if(size >= stackA.getMaxStackSize()){
                size-=stackA.getMaxStackSize();
                stackA.setCount(0);
            }
            else{
                stackA.shrink(size);
                size=0;
            }

            if(size >= stackB.getMaxStackSize()){
                size-=stackB.getMaxStackSize();
                stackB.setCount(0);
            }
            else{
                stackB.shrink(size);
                size=0;
            }
        }
        return res;
    }
}
