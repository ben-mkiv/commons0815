package ben_mkiv.rendertoolkit.common.widgets.component.common;

import ben_mkiv.rendertoolkit.common.widgets.IRenderableWidget;
import ben_mkiv.rendertoolkit.common.widgets.RenderType;
import ben_mkiv.rendertoolkit.common.widgets.WidgetGLWorld;
import ben_mkiv.rendertoolkit.common.widgets.WidgetModifier;
import ben_mkiv.rendertoolkit.common.widgets.core.attribute.IEntity;
import ben_mkiv.rendertoolkit.common.widgets.core.modifiers.WidgetModifierColor;
import ben_mkiv.rendertoolkit.common.widgets.core.modifiers.WidgetModifierRotate;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;


public abstract class EntityWidget extends WidgetGLWorld implements IEntity {
    EntityLivingBase entity = null;

    @Override
    public void writeData(ByteBuf buff) {
        super.writeData(buff);

        if(entity != null) {
            ByteBufUtils.writeTag(buff, entity.serializeNBT());
        }
        else
            ByteBufUtils.writeTag(buff, new NBTTagCompound());
    }

    @Override
    public void readData(ByteBuf buff) {
        super.readData(buff);

        NBTTagCompound entityNBT = ByteBufUtils.readTag(buff);
        if(!entityNBT.equals(new NBTTagCompound()))
            setEntity(entityNBT, Minecraft.getMinecraft().player.world);
        else
            this.entity = null;
    }

    @Override
    public boolean setEntity(EntityLivingBase entity) {
        try {
            NBTTagCompound nbt;
            if(entity instanceof EntityPlayer) {
                nbt = entity.writeToNBT(new NBTTagCompound());
                nbt.setBoolean("isPlayer", true);
                NBTTagCompound gameProfileNBT = NBTUtil.writeGameProfile(new NBTTagCompound(), ((EntityPlayer) entity).getGameProfile());
                nbt.setTag("gameProfile", gameProfileNBT);
            }
            else {
                nbt = entity.serializeNBT();
                nbt.setBoolean("isPlayer", false);
            }

            setEntity(nbt, entity.world);
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    class RenderPlayer extends EntityOtherPlayerMP{

        public RenderPlayer(World world, GameProfile gameProfile){
            super(world, gameProfile);
            // setAlwaysRenderNameTag(false); // as this gets ignored we return an empty name
        }

        @Override
        public ITextComponent getDisplayName(){
            return new TextComponentString("");
        }

    }

    @Override
    public boolean setEntity(NBTTagCompound entityNBT, World world) {
        try {
            if(!entityNBT.getBoolean("isPlayer"))
                this.entity = (EntityLivingBase) EntityList.createEntityFromNBT(entityNBT, world);
            else {
                GameProfile gameProfile = NBTUtil.readGameProfileFromNBT(entityNBT.getCompoundTag("gameProfile"));
                setGameProfile(gameProfile);
                this.entity = new RenderPlayer(DimensionManager.getWorld(0), gameProfile);
                this.entity.readFromNBT(entityNBT);

            }
            this.entity.rotationPitch = 0;
            return true;
        } catch(Exception ex){
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderableWidget getRenderable() {
        return new RenderableEntity();
    }

    public class RenderableEntity extends RenderableGLWidget{
        @Override
        public void render(EntityPlayer player, Vec3d renderOffset, long conditionStates) {
            if(entity == null)
                return;

            preRender(conditionStates);
            GlStateManager.translate(renderOffset.x, renderOffset.y, 70f); //70f to avoid clipping of the entity
            applyModifiers(conditionStates);

            float rotationY = 0;
            boolean hasColor = false;

            for(WidgetModifier modifier : WidgetModifierList.modifiers){
                // remove any rotation on the y axis and apply it in the entity render method
                // this is inaccurate if modifiers should stack -.-
                if(modifier instanceof WidgetModifierRotate){
                    float rotY = ((WidgetModifierRotate) modifier).Y;
                    GlStateManager.rotate(-rotY, 0, 1, 0);
                    rotationY+=rotY;
                }

                // check if we have to set a default color
                if(modifier instanceof WidgetModifierColor)
                    hasColor = true;
            }

            if(!hasColor)
                GlStateManager.color(1, 1, 1, 1);

            if(rendertype == RenderType.WorldLocated) {
                GlStateManager.translate(0.5F, 0.5F, 0.5F);
                GlStateManager.rotate(180, 0, 1, 0);
                if(faceWidgetToPlayer) {
                    GlStateManager.rotate(player.rotationYaw, 0, -1, 0);
                    GlStateManager.rotate(180, 0, 1, 0);
                    GlStateManager.rotate(player.rotationPitch, 1, 0, 0);
                    GlStateManager.rotate(180, 0, 1, 0);
                }
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            }
            else {
                applyAlignments();
                GlStateManager.translate(0F, 1F, 0F);
                GlStateManager.rotate(180, 1, 0, 0);
            }

            renderEntity(renderOffset, rotationY);

            postRender();
        }

        private void applyAlignments(){
            switch(this.getHorizontalAlign()) {
                case CENTER:
                    GL11.glTranslatef(-0.5F, 0F, 0F);
                    break;
                case RIGHT:
                    GL11.glTranslatef(-1F, 0F, 0F);
                    break;
            }

            switch(this.getVerticalAlign()) {
                case MIDDLE:
                    GL11.glTranslatef(0F, -0.5F, 0F);
                    break;
                case BOTTOM:
                    GL11.glTranslatef(0F, -1F, 0F);
                    break;
            }
        }

        void renderEntity(Vec3d location, float rotation) {
            if(entity == null)
                return;

            /*
            if(entity instanceof EntityPlayer){
                ResourceLocation skinLocation;
                SkinManager manager = Minecraft.getMinecraft().getSkinManager();
                Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> skinmap = manager.loadSkinFromCache(((EntityPlayer) entity).getGameProfile());
                if(skinmap.get(MinecraftProfileTexture.Type.SKIN) != null) {
                    skinLocation = manager.loadSkin(skinmap.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                }
                else skinLocation = DefaultPlayerSkin.getDefaultSkinLegacy();

                Minecraft.getMinecraft().getTextureManager().bindTexture(skinLocation);
            }*/

            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();

            GlStateManager.scale(50, 50, 50);

            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);

            entity.rotationYawHead = entity.prevRotationYawHead = entity.rotationYaw = entity.renderYawOffset = -180 + rotation * 360;

            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderShadow(false);
            rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0, 1.0F, false);
            rendermanager.setRenderShadow(true);


            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }

    // thanks to UpcraftLP for the hint on this :)
    private void setGameProfile(GameProfile profile) {
        if(!profile.isComplete() || !profile.getProperties().containsKey("textures")) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getMinecraftSessionService().fillProfileProperties(profile, false);
        }
    }

}
