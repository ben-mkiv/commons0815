package ben_mkiv.rendertoolkit.common.widgets.component.world;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import ben_mkiv.rendertoolkit.common.widgets.WidgetModifierConditionType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetType;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.ITracker;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class EntityTracker3D extends OBJModel3D implements ITracker {
    public enum EntityType{ NONE, ALL, ITEM, LIVING, PLAYER, HOSTILE, NEUTRAL, UNIQUE }
    private static ArrayList<WidgetModifier.WidgetModifierType> applyModifiersList = new ArrayList<>();

    static {
        applyModifiersList.add(WidgetModifier.WidgetModifierType.ROTATE);
        applyModifiersList.add(WidgetModifier.WidgetModifierType.TRANSLATE);
    }

    private static int maximumRange = 128;
    private int trackingRange = 0;
    private EntityType trackingType = EntityType.ALL;
    private String trackingEntityName = "";
    private int trackingEntityMetaIndex = 0;
    private UUID uniqueEntityID = null;

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);
        buff.writeInt(trackingRange);
        buff.writeInt(trackingType.ordinal());

        ByteBufUtils.writeUTF8String(buff, trackingEntityName);
        buff.writeInt(trackingEntityMetaIndex);

        if(uniqueEntityID == null)
            ByteBufUtils.writeUTF8String(buff, "none");
        else
            ByteBufUtils.writeUTF8String(buff, uniqueEntityID.toString());
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);
        trackingRange = Math.min(buff.readInt(), ClientSurface.instances.maxTrackingRange);
        trackingType = EntityType.values()[buff.readInt()];
        setupTracking(trackingType, trackingRange);

        trackingEntityName = ByteBufUtils.readUTF8String(buff);
        trackingEntityMetaIndex = buff.readInt();

        setupTrackingFilter(trackingEntityName, trackingEntityMetaIndex);

        uniqueEntityID = string2UUID(ByteBufUtils.readUTF8String(buff));

        setupTrackingEntity(uniqueEntityID);
    }

    private void setupTracking(int tT, int range) {
        setupTracking(EntityType.values()[tT], range);
    }

    public void setupTracking(EntityType tT, int range){
        this.trackingType = tT;
        this.trackingRange = Math.min(range, maximumRange);
    }

    public void setupTrackingFilter(String type, int metaindex){
        this.trackingEntityName = type.toLowerCase();
        this.trackingEntityMetaIndex = metaindex;
    }

    public void setupTrackingEntity(UUID uuid){
        this.uniqueEntityID = uuid;
    }


    private HashSet<? extends Entity> getAllEntities(Vec3d origin, int distance, World world, Class<? extends Entity> eClass, AxisAlignedBB bounds){
        HashSet<Entity> entities = new HashSet<>();
        for(Entity entity : world.getEntitiesWithinAABB(eClass, bounds)) {
            if(checkDistance(origin, new Vec3d(entity.posX, entity.posY, entity.posZ), distance))
                entities.add(entity);
        }
        return entities;
    }

    @Override
    public WidgetType getType() {
        return WidgetType.ENTITYTRACKER3D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderEntityTracker();
    }

    @SideOnly(Side.CLIENT)
    public class RenderEntityTracker extends RenderableOBJModel{
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(objFile == null) return;
            if(!ClientSurface.instances.entityTrackerEnabled) return;    //require Geolyzer Update

            if(trackingType.equals(EntityType.NONE)) return;

            Entity focusedEntity;
            
            RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
            if(objectMouseOver != null && objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY)){
                focusedEntity = objectMouseOver.entityHit;
            }
            else
                focusedEntity = null;

            Vec3d playerPos = player.getPositionVector();
            AxisAlignedBB bounds = new AxisAlignedBB(playerPos, playerPos).grow(trackingRange);

            GL11.glTranslated(renderOffset.x, renderOffset.y, renderOffset.z);

            this.preRender(conditionStates);

            TESR = Tessellator.getInstance();
            buffer = TESR.getBuffer();

            switch(trackingType) {
                case ALL:
                case ITEM:
                    for (Entity e : getAllEntities(player.getPositionVector(), trackingRange, player.world, EntityItem.class, bounds)) {
                        if (!checkRender(e)) continue;
                        renderTarget(e, player, customRenderConditions(conditionStates, e, focusedEntity));
                    }
            }

            switch(trackingType) {
                case ALL:
                case PLAYER:
                case LIVING:
                    for (Entity e : getAllEntities(player.getPositionVector(), trackingRange, player.world, EntityPlayer.class, bounds)) {
                        if (!checkRender(e)) continue;
                        if (e == player) continue;
                        renderTarget(e, player, customRenderConditions(conditionStates, e, focusedEntity));
                    }
            }

            switch(trackingType) {
                case ALL:
                case LIVING:
                case NEUTRAL:
                case HOSTILE:
                    for (Entity e : getAllEntities(player.getPositionVector(), trackingRange, player.world, EntityLiving.class, bounds)) {
                        if(!checkRender(e)) continue;
                        renderTarget(e, player, customRenderConditions(conditionStates, e, focusedEntity));
            }   }

            this.postRender();
        }

        long customRenderConditions(long customConditions, Entity e, Entity focusedEntity){
            if(focusedEntity != null && focusedEntity.getPersistentID().equals(e.getPersistentID()))
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_FOCUSED_ENTITY);

            if(e instanceof EntityMob)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_HOSTILE);

            if(!(e instanceof EntityMob) && (e instanceof EntityLivingBase))
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_NEUTRAL);

            if(e instanceof EntityLivingBase)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_LIVING);

            if(e instanceof EntityItem)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_ITEM);

            if(e instanceof EntityPlayer)
                customConditions |= ((long) 1 << WidgetModifierConditionType.IS_PLAYER);

            return customConditions;
        }


        boolean checkRender(Entity e){
            switch(trackingType) {
                case ALL:
                    return true;

                case UNIQUE:
                    return e.getUniqueID().equals(uniqueEntityID);

                case HOSTILE:
                    if(!(e instanceof EntityMob))
                        return false;

                    if(trackingEntityName.length() > 0)
                        return e.getName().toLowerCase().equals(trackingEntityName);

                    return true;

                case PLAYER:
                case LIVING:
                    if(!(e instanceof EntityLivingBase))
                        return false;

                    if(trackingEntityName.length() > 0)
                        return e.getName().toLowerCase().equals(trackingEntityName);

                    return true;

                case NEUTRAL:
                    if(!(e instanceof EntityLivingBase))
                        return false;

                    if(e instanceof EntityMob)
                        return false;

                    if(trackingEntityName.length() > 0)
                        return e.getName().toLowerCase().equals(trackingEntityName);

                    return true;

                case ITEM:
                    if(!(e instanceof EntityItem))
                        return false;

                    if(trackingEntityName.length() == 0)
                        return true;

                    if(trackingEntityMetaIndex != -1 && ((EntityItem) e).getItem().getMetadata() != trackingEntityMetaIndex)
                        return false;

                    return trackingEntityName.equalsIgnoreCase(((EntityItem) e).getItem().getItem().getRegistryName().toString());

                default:
                    return false;
            }
        }

        void renderTarget(Entity entity, EntityPlayer player, long conditionStates) {
            GlStateManager.pushMatrix();
            Vec3d pos = entity.getPositionVector();
            GL11.glTranslated(pos.x, pos.y, pos.z);

            this.applyModifierList(conditionStates, applyModifiersList);

            int color = WidgetModifierList.getCurrentColor(conditionStates, 0);

            if(faceWidgetToPlayer){
                GL11.glRotated( 180 - player.rotationYaw, 0.0D, 1.0D, 0.0D);
                GL11.glRotated( -player.rotationPitch, 1.0D, 0.0D, 0.0D);
            }

            if(objFile.facesTri.size() > 0) {
                buffer.begin(GL11.GL_TRIANGLES, malisisVertexFormat);
                renderList(objFile.facesTri, color);
                TESR.draw();
            }

            if(objFile.facesQuad.size() > 0) {
                buffer.begin(GL11.GL_QUADS, malisisVertexFormat);
                renderList(objFile.facesQuad, color);
                TESR.draw();
            }

            GlStateManager.popMatrix();
        }
    }

    private static boolean checkDistance(Vec3d src, Vec3d target, int range){
        return src.distanceTo(target) <= range;
    }

}
