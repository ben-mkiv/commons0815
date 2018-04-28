package ben_mkiv.rendertoolkit.surface;

import ben_mkiv.rendertoolkit.client.renderer.ClientRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ClientSurface extends ClientRenderer {
    public static ClientSurface instances = new ClientSurface();

    public boolean shouldRenderStart(boolean renderingOverlay) {
        return true;
    }

    @Override
    public long getConditionStates(EntityPlayer player){ return Long.MIN_VALUE; }

    @Override
    public Vec3d getRenderOffset(){
        return new Vec3d(0, 0, 0);
    }

}

