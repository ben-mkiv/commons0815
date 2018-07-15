package ben_mkiv.commons0815.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class IEnergyStorageAdvanced implements IEnergyStorage {
    EnergyStorage storage;
    TileEntity tile;

    public IEnergyStorageAdvanced(TileEntity te, int capacity, int maxReceive, int maxExtract){
        this.tile = te;
        this.storage = new DynamicEnergyStorage(capacity, maxReceive, maxExtract);
    }

    class DynamicEnergyStorage extends EnergyStorage {
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

    public IEnergyStorageAdvanced(TileEntity te, int capacity) {
        this(te, capacity, capacity, capacity);
    }

    @Override
    public boolean canExtract(){
        return false;
    }

    public int getTransferrate(){
        return ((DynamicEnergyStorage) storage).getTransferrate();
    }

    @Override
    public boolean canReceive(){
        return storage.canReceive();
    }

    @Override
    public int getMaxEnergyStored(){
        return storage.getMaxEnergyStored();
    }

    @Override
    public int getEnergyStored(){
        return storage.getEnergyStored();
    }

    public void setCapacity(int newCapacity){
        ((DynamicEnergyStorage) storage).setCapacity(newCapacity);
    }

    public void setTransferrate(int newTransferrate){
        ((DynamicEnergyStorage) storage).setTransferrate(newTransferrate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate){
        int received = storage.receiveEnergy(maxReceive, simulate);
        tile.markDirty();
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate){
        int extracted = storage.extractEnergy(maxExtract, simulate);
        tile.markDirty();
        return extracted;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        return ((DynamicEnergyStorage) storage).writeToNBT(nbt);
    }

    public void readFromNBT(NBTTagCompound nbt){
        ((DynamicEnergyStorage) storage).readFromNBT(nbt);
    }


}
