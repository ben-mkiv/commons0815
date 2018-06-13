package ben_mkiv.commons0815.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class HotkeyEntityEvent extends hotkeyEvent {
    UUID targetEntity = null;
    final int range = 10;

    public HotkeyEntityEvent(UUID targetEntity, String action){
        super(action);
        this.targetEntity = targetEntity;
    }

    public HotkeyEntityEvent(NBTTagCompound nbt){
        super(nbt);
    }

    @Override
    public boolean canExecute(EntityPlayer player){
        if(targetEntity == null)
            return false;

        Entity target = getEntity();

        if(target.equals(player))
            return super.canExecute(player);

        if(player.world.provider.getDimension() != target.world.provider.getDimension() || player.getDistance(target) > range){
            player.sendStatusMessage(new TextComponentString("entity out of range"), true);
            return false;
        }

        return super.canExecute(player);
    }

    public Entity getEntity(){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(targetEntity);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt = super.writeToNBT(nbt);
        nbt.setUniqueId("targetUUID", targetEntity);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        this.targetEntity = nbt.getUniqueId("targetUUID");
    }

}
