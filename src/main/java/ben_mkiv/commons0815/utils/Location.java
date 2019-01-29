package ben_mkiv.commons0815.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;

public class Location {
	public BlockPos pos;
	public int dimID;
	public UUID uniqueKey;
	public Location(BlockPos pos, int dimID, UUID uniqueKey) {
		this.pos = pos;
		this.dimID = dimID;
		this.uniqueKey = uniqueKey;
	}

	public Location() {}

	@Override
	public boolean equals(Object arg0) {
		if(!(arg0 instanceof Location))
			return false;

		if(!((Location)arg0).pos.equals(pos))
			return false;

		if(((Location)arg0).dimID != dimID)
			return false;

		return ((Location)arg0).uniqueKey.equals(uniqueKey);
	}
	
	@Override
	public String toString() {
		return "X:" +pos.getX() +" Y:" + pos.getY() + " Z:" + pos.getZ() + " DIM:"+dimID + "\n Key:"+uniqueKey;
	}
	

	public TileEntity getTileEntity(){
		return utilsCommon.getTileEntity(dimID, pos);
	}

	public Location readFromNBT(NBTTagCompound nbt) {
		pos = new BlockPos(nbt.getInteger("locX"), nbt.getInteger("locY"), nbt.getInteger("locZ"));
		dimID = nbt.getInteger("locDim");
		uniqueKey = nbt.getUniqueId("uniqueKey");
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("locX", pos.getX());
		nbt.setInteger("locY", pos.getY());
		nbt.setInteger("locZ", pos.getZ());
		nbt.setInteger("locDIM", dimID);
		nbt.setUniqueId("uniqueKey", uniqueKey);
		return nbt;
	}
}
