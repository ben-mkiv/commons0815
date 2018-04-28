package ben_mkiv.rendertoolkit.client;

import ben_mkiv.rendertoolkit.client.event.ClientEventHandler;
import ben_mkiv.rendertoolkit.network.EventType;
import ben_mkiv.rendertoolkit.network.messages.ClientEventPacket;
import ben_mkiv.rendertoolkit.network.rTkNetwork;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class interactGuiScreen extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(ClientSurface.renderResolution != null){
            mouseX*=(ClientSurface.renderResolution.x / ClientSurface.resolution.getScaledWidth());
            mouseY*=(ClientSurface.renderResolution.y / ClientSurface.resolution.getScaledHeight());
        }

        rTkNetwork.channel.sendToServer(new ClientEventPacket(EventType.INTERACT_OVERLAY, mc.player, new Vec3d(mouseX, mouseY, 0), mouseButton));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if(!Keyboard.isKeyDown(ClientEventHandler.interactGUIKey.getKeyCode())){
            mc.displayGuiScreen(null);
        }
    }
}
