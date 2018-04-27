package ben_mkiv.ocUtils;

import ben_mkiv.rendertoolkit.renderToolkit;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.item.*;
import li.cil.oc.api.network.EnvironmentHost;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class CustomDriver {
    public static ArrayList<DriverItem> drivers = new ArrayList<>();

    //Data tags for OC components
    public static NBTTagCompound dataTag(ItemStack stack){
        final String tagName = renderToolkit.MODID + "data";

        if(!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey(tagName))
            nbt.setTag(tagName, new NBTTagCompound());

        return nbt.getCompoundTag(tagName);
    }

    public static DriverItem driverFor(ItemStack stack, Class<? extends EnvironmentHost> host){
        for(DriverItem driver : CustomDriver.drivers)
            if(driver instanceof HostAware)
                if(((HostAware) driver).worksWith(stack)) //, host
                    return driver;

        return Driver.driverFor(stack, host);
    }

    public static DriverItem driverFor(ItemStack stack){
        for(DriverItem driver : drivers)
            if(driver.worksWith(stack))
                return driver;

        return Driver.driverFor(stack);
    }

}