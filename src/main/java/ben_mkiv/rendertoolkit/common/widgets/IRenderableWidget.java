package ben_mkiv.rendertoolkit.common.widgets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public interface IRenderableWidget {
	void render(EntityPlayer player, Vec3d location, long conditionStates);
	RenderType getRenderType();
	boolean shouldWidgetBeRendered(EntityPlayer player);
	boolean shouldWidgetBeRendered(EntityPlayer player, Vector3f offset);
	UUID getWidgetOwner();
	boolean isWidgetOwner(String uuid);
}
