package ben_mkiv.commons0815.utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class utilsClient {

    private static HashMap<String, Long> cooldownTimer = new HashMap<>();

    public static FontRenderer fontRenderer(){
        return Minecraft.getMinecraft().fontRenderer;
    }

    public static float defaultFOV = Minecraft.getMinecraft().gameSettings.fovSetting;

    public static void setFOV(float fov){
        Minecraft.getMinecraft().gameSettings.fovSetting = fov;
    }

    public static Entity getFocusedEntity(){
        EntityPlayer player = Minecraft.getMinecraft().player;
        return player.isSneaking() ? player : Minecraft.getMinecraft().objectMouseOver.entityHit;
    }

    public static TileEntity getFocusedTileEntity() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver == null)
            return null;

        if(!mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK))
            return null;

        return mc.getMinecraft().player.world.getTileEntity(mc.getMinecraft().objectMouseOver.getBlockPos());
    }

    public static Entity getEntityLookingAt(){
        RayTraceResult objectMouseOver = Minecraft.getMinecraft().player.rayTrace(128, 1);
        if(objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY){
            return objectMouseOver.entityHit;
        }
        return null;
    }

    public static boolean checkCooldown(String identifier) {
        return checkCooldown(identifier, 500);
    }

    public static boolean checkCooldown(String identifier, long cooldownTime){
        long timenow = net.minecraft.client.Minecraft.getSystemTime();

        long timeout = 0;
        if(cooldownTimer.get(identifier) != null)
            timeout = cooldownTimer.get(identifier);

        if(timenow - timeout < cooldownTime)
            return false;

        cooldownTimer.remove(identifier);
        cooldownTimer.put(identifier, timenow);
        return true;
    }

}
