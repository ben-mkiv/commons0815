package ben_mkiv.commons0815.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class hotkeyEvent {
    public String action = "unknown";

    public ArrayList<String> group = new ArrayList<>();

    public hotkeyEvent(String action){
        this.action = action;
    }

    public hotkeyEvent(NBTTagCompound nbt){
        this("readFromNBT");
        readFromNBT(nbt);
    }

    public boolean execute(EntityPlayer player){ return false; }

    public boolean canExecute(EntityPlayer player){
        return true;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setString("action", this.action);

        for(int i=0; nbt.hasKey("group"+i); i++)
            nbt.removeTag("group"+i);

        for(int i=0; i < group.size(); i++)
            nbt.setString("group"+i, group.get(i));

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt){
        this.action = nbt.getString("action");
        this.group.clear();

        for(int i=0; nbt.hasKey("group"+i); i++)
            this.group.add(nbt.getString("group"+i));
    }


}
