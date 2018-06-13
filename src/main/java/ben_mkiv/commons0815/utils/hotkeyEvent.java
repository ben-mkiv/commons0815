package ben_mkiv.commons0815.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class hotkeyEvent {
    public String action = "unknown";
    public int key = -1;

    public ArrayList<String> group = new ArrayList<>();

    public hotkeyEvent(String action){
        this.action = action;
    }

    public hotkeyEvent(NBTTagCompound nbt){
        readFromNBT(nbt);
    }

    public boolean execute(EntityPlayer player){ return false; }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("key", this.key);
        nbt.setString("action", this.action);
        for(int i=0; i < group.size(); i++)
            nbt.setString("group"+i, group.get(i));

        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt){
        this.key = nbt.getInteger("key");
        this.action = nbt.getString("action");

        group.clear();
        int i=0;
        while(nbt.hasKey("group"+i))
            group.add(nbt.getString("group"+i));
    }

}
