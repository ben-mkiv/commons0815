package ben_mkiv.commons0815.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class DynamicEnergyStorage extends EnergyStorage {
    public DynamicEnergyStorage(int capacity){
        super(capacity);
    }

    public DynamicEnergyStorage(int capacity, int maxReceive, int maxExtract){
        super(capacity, maxReceive, maxExtract);
    }

    public void setCapacity(int newCapacity){
        this.capacity = newCapacity;
    }

    public void setTransferrate(int newTransferrate){
        this.maxReceive = this.maxExtract = newTransferrate;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        if(nbt == null)
            nbt = new NBTTagCompound();

        nbt.setInteger("capacity", getMaxEnergyStored());
        nbt.setInteger("stored", getEnergyStored());
        nbt.setInteger("transferrate", Math.max(this.maxReceive, this.maxExtract));

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt){
        if(nbt == null)
            return;

        this.energy = nbt.getInteger("stored");

        setTransferrate(nbt.getInteger("transferrate"));
        setCapacity(nbt.getInteger("capacity"));
    }

    public int getTransferrate(){
        return Math.min(maxExtract, maxReceive);
    }
}
