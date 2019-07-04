package ben_mkiv.commons0815.utils;

import com.google.common.base.Charsets;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.vecmath.Vector3f;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class utilsCommon {
	/*
	public static Entity getEntityByUUID(World world, UUID uuid){
		for(Entity ent : world.getLoadedEntityList())
			if(ent.getUniqueID().equals(uuid))
				return ent;

		return null;
	}

	// if this is required put it in a proxy, as it would break servercode here, leaving it in here for reference
	public static Entity getEntityByUUID(UUID uuid){
		if(FMLCommonHandler.instance().getEffectiveSide().equals(Side.SERVER))
			return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(uuid);
		else
			return getEntityByUUID(net.minecraft.client.Minecraft.getMinecraft().world, uuid);
	}
	*/

	public static ArrayList<BlockPos> getSquareOffsets(int radius){
		ArrayList<BlockPos> list = new ArrayList();
		for(int row = -radius; row <= radius; row++)
			for(int col = -radius; col <= radius; col++) {
				ArrayList<Integer> l = new ArrayList();
				list.add(new BlockPos(row, 0, col));
			}

		return list;
	}

	public static TileEntity getTileEntity(int dimensionId, BlockPos pos){
		World world  = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
		if(world==null)
			return null;
		return world.getTileEntity(pos);
	}

	public static Map<BlockPos, Block> getSquareBlocks(World world, BlockPos pos, int radius){
		Map<BlockPos, Block> list = new HashMap<>();

		for(BlockPos val : getSquareOffsets(radius))
			list.put(val, world.getBlockState(new BlockPos(pos.getX() + val.getX(), pos.getY(), pos.getZ() + val.getZ())).getBlock());

		return list;
	}

	public static ArrayList<Class> getClassHierarchy(Class p) {
		ArrayList<Class> list = new ArrayList<>();
		list.add(p);
		while((p = p.getSuperclass()) != null)
			list.add(p);

		return list;
	}

	// modClazz was UrbanMechs.class
	public static ArrayList<String> readResourceLocation(Class modClazz, String asset){
		InputStream is = null;
		try {
			is = modClazz.getClassLoader().getResourceAsStream("assets/urbanmechs" + asset);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
			final ArrayList<String> lines = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		} catch (Throwable ignored) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public static ArrayList<String> readResourceLocation(Class modClazz, ResourceLocation rl) {
		return readResourceLocation(modClazz, rl.getResourcePath());
	}

	public static String StringFromResourceLocation(Class modClazz, ResourceLocation rl){
		return list2string(readResourceLocation(modClazz, rl));
	}

	public static String list2string(ArrayList<String> list){
		String output = "";

		if(list != null)
			for(String append : list)
				output+="\n"+append;

		return output;
	}

	public static int getIntFromColor(float red, float green, float blue, float alpha){
	    return new Color(red, green, blue, alpha).getRGB();
	}

	public static int getIntFromColor(float color[], float alpha){
		return new Color(color[0], color[1], color[2], alpha).getRGB();
	}

	public static Vector3f getVector3fFromColor(int color){
		Color col = new Color(color);
		return new Vector3f(col.getRed(), col.getGreen(),col.getBlue());
	}

	public static int getIntFromColor(float red, float green, float blue){
		return getIntFromColor(red, green, blue, 0);
	}
	
	public static boolean isIntColorVisible(int color){
		return (color & 0x000000FF) > 0;
	}

	public static float deg2rad(float deg){
		//return ((float) Math.PI * deg / 180);
		return (float) Math.toRadians(deg);
	}
	
	public static boolean inRange(double x, double y, double z, double sx, double sy, double sz, double r){
		return (((x-sx)*(x-sx)) + ((y-sy)*(y-sy)) + ((z-sz)*(z-sz))) <= (r*r);
	}
	
	public static boolean inRange(Entity player, double sx, double sy, double sz, double r){
		return player.getPositionVector().distanceTo(new Vec3d(sx, sy, sz)) <= r;
		//return inRange(player.posX, player.posY, player.posZ, sx, sy, sz, r);
	}

	public static boolean inRange(Entity player, Vec3d pos, double r){
		return player.getPositionVector().distanceTo(pos) <= r;
		//return inRange(player.posX, player.posY, player.posZ, pos.x, pos.y, pos.z, r);
	}
	
	public static boolean isLookingAt(RayTraceResult pos, float[] target){
		if(pos == null) return false;
		if(pos.getBlockPos().getX() != target[0]) return false;
		if(pos.getBlockPos().getY() != target[1]) return false;
		if(pos.getBlockPos().getZ() != target[2]) return false;
					
		return true;
	}

	public static RayTraceResult rayTrace(Entity entity, double range, float tickAge){
		Vec3d vec3d = entity.getPositionEyes(tickAge);
		Vec3d vec3d1 = entity.getLook(tickAge);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range);

		return entity.getEntityWorld().rayTraceBlocks(vec3d, vec3d2);
	}

	public static int getLightLevelPlayer(EntityPlayer e){
		return e.world.getLightFor(EnumSkyBlock.SKY, new BlockPos(e.posX, e.posY + 1, e.posZ));
	}

	public static boolean isPlayerSwimming(EntityPlayer e){
		if(e.world.getBlockState(new BlockPos(e.posX, e.posY, e.posZ)).getMaterial().isLiquid()
				|| e.world.getBlockState(new BlockPos(e.posX, e.posY+1, e.posZ)).getMaterial().isLiquid())
			return true;

		return false;
	}


}
