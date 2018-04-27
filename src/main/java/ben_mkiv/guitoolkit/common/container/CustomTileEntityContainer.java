package ben_mkiv.guitoolkit.common.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class CustomTileEntityContainer extends Container {
    TileEntity tileEntity;
    EntityPlayer player;

    public CustomTileEntityContainer(TileEntity te, EntityPlayer p) {
        this.tileEntity = te;
        this.player = p;
    }

    public TileEntity getEntity(){ return this.tileEntity; }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canInteractWith(EntityPlayer player) { return true; }


}
