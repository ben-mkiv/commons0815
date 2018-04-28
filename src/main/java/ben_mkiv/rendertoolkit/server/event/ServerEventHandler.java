package ben_mkiv.rendertoolkit.server.event;

import ben_mkiv.rendertoolkit.network.EventType;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.logging.Logger;

public class ServerEventHandler {
    protected int tick = 0;

    public ServerEventHandler() {}

   @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
        if(!e.getSide().isServer()) return;
        onInteractEvent(EventType.INTERACT_WORLD_BLOCK_LEFT, e);
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem e){
        if(!e.getSide().isServer()) return;
        onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e){
        if(!e.getSide().isServer()) return;
        onInteractEvent(EventType.INTERACT_WORLD_BLOCK_RIGHT, e);
    }

    protected void onInteractEvent(EventType type, PlayerInteractEvent event){
        Logger.getLogger(renderToolkit.MODID).warning("server side onInteractEvent");
    }

}
