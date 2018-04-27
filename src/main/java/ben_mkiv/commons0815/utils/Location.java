package ben_mkiv.commons0815.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class Location {
	public int x,y,z,dimID;
	public UUID uniqueKey;
	public Location(BlockPos pos, int dimID, UUID uniqueKey) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.dimID = dimID;
		this.uniqueKey = uniqueKey;
	}

	public Location() {}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Location){
			if( ((Location)arg0).x == x && ((Location)arg0).y == y && ((Location)arg0).z == z && 
					((Location)arg0).dimID == dimID && ((Location)arg0).uniqueKey == uniqueKey){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "X:" +x +" Y:" + y + " Z:" + z + " DIM:"+dimID + "\n Key:"+uniqueKey;
	}
	
	public String[] toArrayString() {
		return new String[]{"X:" +x + " Y:" + y + " Z:" + z, "DIM:"+dimID, "Key:"+uniqueKey};
	}
	
	public TileEntity getTileEntity(){
		return utilsCommon.getTileEntity(dimID, new BlockPos(x, y, z));
	}

	public Location readFromNBT(NBTTagCompound nbt) {
		x = nbt.getInteger("locX");
		y = nbt.getInteger("locY");
		z = nbt.getInteger("locZ");
		dimID = nbt.getInteger("locDim");
		uniqueKey = nbt.getUniqueId("uniqueKey");
		return this;
	}

	public Location writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("locX", x);
		nbt.setInteger("locY", y);
		nbt.setInteger("locZ", z);
		nbt.setInteger("locDIM", dimID);
		nbt.setUniqueId("uniqueKey", uniqueKey);
		return this;
	}
}
