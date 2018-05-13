package ben_mkiv.commons0815.utils;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtils {
    //tries to move x amount of FE from one capability to another
    public static int transferEnergy(IEnergyStorage source, IEnergyStorage destination, int amount, boolean simulate){
        amount = source.extractEnergy(Math.min(amount, source.getEnergyStored()), simulate);
        int transfered = destination.receiveEnergy(amount, simulate);
        if(transfered < amount)
            source.receiveEnergy(amount - transfered, simulate);

        return transfered;
    }

    //tries to move the maximum possible amount of FE from one capability to another
    public static int transferEnergy(IEnergyStorage source, IEnergyStorage destination, boolean simulate) {
        return transferEnergy(source, destination, destination.getMaxEnergyStored() - destination.getEnergyStored(), simulate);
    }

    public static void setEnergyStored(IEnergyStorage storage, int energy) {
        int transfer = energy - storage.getEnergyStored();
        if (transfer > 0) {
            storage.receiveEnergy(transfer, false);
        } else if (transfer < 0) {
            storage.extractEnergy(-transfer, false);
        }
    }

}
