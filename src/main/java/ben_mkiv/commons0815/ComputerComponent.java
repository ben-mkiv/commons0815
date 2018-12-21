package ben_mkiv.commons0815;

import li.cil.oc.api.API;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.network.*;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import java.util.Map;

abstract public class ComputerComponent implements ManagedEnvironment, ITickable, DeviceInfo {
    protected Node node;

    protected Entity entity;
    protected String componentName = "defaultComponentName";

    protected Map<String, String> deviceInfo;

    protected String getComponentName() {
        return this.componentName;
    }

    public void setupNode(){
        if(this.node() != null) return;
        this.node = API.network.newNode(this, Visibility.Network).withComponent(getComponentName()).create();
    }

    @Override
    public Node node() {
        return this.node;
    }

    @Override
    public void onConnect(Node node) {
    }

    @Override
    public void onDisconnect(Node node) {
    }

    @Override
    public void onMessage(Message message) {}

    @Override
    public void load(NBTTagCompound nbt) {
        if(nbt.hasKey("node")) node.load(nbt.getCompoundTag("node"));
    }

    @Override
    public void save(NBTTagCompound nbt) {
        setupNode();
        if(node == null) return;
        NBTTagCompound nodeTag = new NBTTagCompound();
        node.save(nodeTag);
        nbt.setTag("node", nodeTag);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void update() {}

}
