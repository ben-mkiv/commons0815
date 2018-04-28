package ben_mkiv.rendertoolkit.proxy;

import ben_mkiv.commons0815.utils.PlayerStats;
import ben_mkiv.rendertoolkit.surface.ClientSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void registermodel(Item item, int meta){
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @Override
    public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {
        //ManualPathProvider.initialize();
    }

    @Override
    public World getWorld(int dimensionId) {
        if (getCurrentClientDimension() != dimensionId) {
            return null;
        } else
            return Minecraft.getMinecraft().world;
    }

    @Override
    public EntityPlayer getPlayer(String username) {
        return Minecraft.getMinecraft().player;
    }


    @Override
    public PlayerStats getPlayerStats(String playerName) {
        return getPlayerStats(getPlayer(playerName).getUniqueID());
    }


    @Override
    public PlayerStats getPlayerStats(UUID uuid) {
        PlayerStats s = new PlayerStats(getPlayer(""));
        s.setScreen(ClientSurface.resolution.getScaledWidth(), ClientSurface.resolution.getScaledHeight(), (double) ClientSurface.resolution.getScaleFactor());
        return s;
    }

    @Override
    public int getCurrentClientDimension() {
        return Minecraft.getMinecraft().world.provider.getDimension();
    }

}
