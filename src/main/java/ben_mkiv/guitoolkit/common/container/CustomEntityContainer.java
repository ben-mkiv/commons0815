package ben_mkiv.guitoolkit.common.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
public class CustomEntityContainer extends Container {
    protected Entity entity;
    protected EntityPlayer player;

    public CustomEntityContainer(Entity e, EntityPlayer p) {
        this.entity = e;
        this.player = p;
    }

    public Entity getEntity(){ return this.entity; }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canInteractWith(EntityPlayer player) { return true; }

}
