package ben_mkiv.commons0815.utils;

import li.cil.oc.api.driver.DeviceInfo.DeviceAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Vexatos
 */
public class OCUtils {

    public static NBTTagCompound dataTag(final ItemStack stack) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        final NBTTagCompound nbt = stack.getTagCompound();
        // This is the suggested key under which to store item component data.
        // You are free to change this as you please.
        if(!nbt.hasKey("oc:data")) {
            nbt.setTag("oc:data", new NBTTagCompound());
        }
        return nbt.getCompoundTag("oc:data");
    }

    public static class Device {

        private final String Class;
        private final String Description;
        private final String Vendor;
        private final String Product;
        private final String[] other;

        public Device(String Class, String Description, String Vendor, String Product, String... other) {
            this.Class = Class;
            this.Description = Description;
            this.Vendor = Vendor;
            this.Product = Product;
            this.other = other;
        }


        public Map<String, String> deviceInfo() {
            Map<String, String> deviceInfo = new HashMap<String, String>();
            deviceInfo.put(DeviceAttribute.Class, Class);
            deviceInfo.put(DeviceAttribute.Description, Description);
            deviceInfo.put(DeviceAttribute.Vendor, Vendor);
            deviceInfo.put(DeviceAttribute.Product, Product);
            for(int i = 0; i + 1 < other.length; i += 2) {
                deviceInfo.put(other[i], other[i + 1]);
            }
            return deviceInfo;
        }
    }


}