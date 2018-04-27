package ben_mkiv.commons0815.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientUtils {

    public static Entity getEntityLookingAt(){
        if(Minecraft.getMinecraft().objectMouseOver == null)
            return null;

        if(!Minecraft.getMinecraft().objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY))
            return null;

        return Minecraft.getMinecraft().objectMouseOver.entityHit;
    }

    public static RayTraceResult getBlockCoordsLookingAt(EntityPlayer player){
        RayTraceResult objectMouseOver = player.rayTrace(200, 1);
        if(objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK){
            return objectMouseOver;
        }
        return null;
    }

    public Entity rayTraceEntity(World world, Entity entity) {
        double reachDistance = 5;
        Vec3d vec3d = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3d vec3d1 = entity.getLookVec();
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * reachDistance, vec3d1.y * reachDistance, vec3d1.z * reachDistance);

        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand(vec3d1.x * reachDistance, vec3d1.y * reachDistance, vec3d1.z * reachDistance).expand(1.0D, 1.0D, 1.0D));

        for (int j = 0; j < list.size(); ++j){
            Entity entity1 = list.get(j);
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)entity1.getCollisionBorderSize(),(double)entity1.getCollisionBorderSize(),(double)entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

            if (!axisalignedbb.intersects(vec3d, vec3d2))
                if (raytraceresult != null)
                    return raytraceresult.entityHit;

        }

        return null;
    }

    private static final ResourceLocation selection = new ResourceLocation( "opencomputers" , "textures/gui/robot_selection.png");

    public static void drawSelection(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(selection);
        double now = System.currentTimeMillis() / 1000.0;
        double offsetV = (int)((now % 1) * 17) / 17D;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder vertexbuffer = t.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x, y+20, 0).tex(0, offsetV + 1D/17D).endVertex();
        vertexbuffer.pos(x+20, y+20, 0).tex(1, offsetV + 1D/17D).endVertex();
        vertexbuffer.pos(x+20, y, 0).tex(1, offsetV).endVertex();;
        vertexbuffer.pos(x, y, 0).tex(0, offsetV).endVertex();
        t.draw();
    }
}
