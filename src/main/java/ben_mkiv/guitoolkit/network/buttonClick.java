package ben_mkiv.guitoolkit.network;

import ben_mkiv.guitoolkit.client.widget.prettyButton;
import ben_mkiv.guitoolkit.common.guiHandler;
import ben_mkiv.guitoolkit.common.guiWindow;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class buttonClick implements IMessage {

    public NBTTagCompound nbt = new NBTTagCompound();

    public buttonClick(){}

    @SideOnly(Side.CLIENT)
    public buttonClick(GuiButton button){
        if(button instanceof prettyButton) {
            this.nbt.setString("label", button.displayString);
            this.nbt.setString("action", ((prettyButton) button).action);
        }
        if(button instanceof guiWindow.screenLinkButton){
            guiWindow.screenLinkButton btn = (guiWindow.screenLinkButton) button;
            this.nbt.setInteger("screenLinkButton", guiHandler.getIndex(btn.screen));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbt);
    }
}

