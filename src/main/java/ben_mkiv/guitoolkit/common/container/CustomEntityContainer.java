package ben_mkiv.guitoolkit.common.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
public class CustomEntityContainer extends Container {
    protected Entity entity;
    protected EntityPlayer player;
    protected TileEntity te;

    public CustomEntityContainer(Entity e, EntityPlayer p) {
        this.entity = e;
        this.player = p;
    }

    public CustomEntityContainer(TileEntity tileEntity, EntityPlayer p) {
        this.te = tileEntity;
        this.player = p;
    }

    public Entity getEntity(){ return this.entity; }

    public TileEntity getTileEntity(){ return this.te; }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canInteractWith(EntityPlayer player) { return true; }

}
