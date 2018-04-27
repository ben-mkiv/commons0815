package ben_mkiv.rendertoolkit.proxy;

import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.rendertoolkit.renderToolkit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class CommonProxy {
    public void registermodel(Item item, int meta){}

    public void preInit() {}

    public void init() {}

    public void postInit() {}

    public World getWorld(int dimensionId) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
    }

    public IModel registerModel(ResourceLocation model){
        return null;
    }

    public EntityPlayer getPlayer(String username){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username);
    }

    public PlayerStats getPlayerStats(UUID uuid) {
        return renderToolkit.ServerSurface.instance.playerStats.get(uuid);
    }

    public PlayerStats getPlayerStats(String playerName) {
        return getPlayerStats(getPlayer(playerName).getUniqueID());
    }

    public int getCurrentClientDimension() {
        return -9001;
    }

    public boolean isServer(){
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public boolean isClient(){
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }
}
