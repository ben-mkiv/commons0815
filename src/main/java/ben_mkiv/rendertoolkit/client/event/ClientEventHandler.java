package ben_mkiv.rendertoolkit.client.event;

import ben_mkiv.rendertoolkit.client.interactGuiScreen;
import ben_mkiv.rendertoolkit.client.thermalvision.ShaderHelper;
import ben_mkiv.rendertoolkit.network.EventType;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.logging.Logger;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    public static KeyBinding interactGUIKey = new KeyBinding("key.interact", Keyboard.KEY_C, "key.categories." + renderToolkit.MODID.toLowerCase());

    public ClientEventHandler() {
        ClientRegistry.registerKeyBinding(interactGUIKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new interactGuiScreen());
    }

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e){
        onInteractEvent(EventType.INTERACT_WORLD_LEFT, e);
    }

    @SubscribeEvent
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e){
        onInteractEvent(EventType.INTERACT_WORLD_RIGHT, e);
    }

    protected void onInteractEvent(EventType type, PlayerInteractEvent event){
        if(event.getSide().isServer()) return;
        Logger.getLogger(renderToolkit.MODID).warning("client side onInteractEvent");
    }



}
