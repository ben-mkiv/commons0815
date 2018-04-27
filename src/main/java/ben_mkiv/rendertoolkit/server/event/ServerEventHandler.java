package ben_mkiv.rendertoolkit.server.event;

import ben_mkiv.rendertoolkit.network.EventType;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.logging.Logger;

    public class ServerEventHandler {
        //public static KeyBinding interactGUIKey = new KeyBinding("key.interact", Keyboard.KEY_C, "key.categories." + renderToolkit.MODID.toLowerCase());
        protected int tick = 0;

        public ServerEventHandler() {
            //ClientRegistry.registerKeyBinding(interactGUIKey);
        }

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent e){}

        @SubscribeEvent
        public void onJoin(EntityJoinWorldEvent e){}

        @SubscribeEvent
        public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
            onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
        }

        @SubscribeEvent
        public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e){
            onInteractEvent(EventType.INTERACT_WORLD_BLOCK_LEFT, e);
        }

        @SubscribeEvent
        public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e){
            onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
        }

        @SubscribeEvent
        public void onRightClickItem(PlayerInteractEvent.RightClickItem e){
            onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
        }

        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e){
            onInteractEvent(EventType.INTERACT_WORLD_BLOCK_RIGHT, e);
        }

        protected void onInteractEvent(EventType type, PlayerInteractEvent event){
            Logger.getLogger(renderToolkit.MODID).warning("server side onInteractEvent");
        }



}
