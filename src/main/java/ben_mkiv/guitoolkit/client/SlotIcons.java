package ben_mkiv.guitoolkit.client;

import li.cil.oc.api.driver.item.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;


public class SlotIcons {
    private static Map<Integer, ResourceLocation > tiericons;
    private static Map<String, ResourceLocation > sloticons;

    private static void registerSlotTypeIcons(){
        if(sloticons != null) return;

        String[] SLOT_TYPES  = new String[]{
                Slot.Card, Slot.ComponentBus, Slot.Container, Slot.CPU, Slot.Floppy,
                Slot.HDD, Slot.Memory, Slot.RackMountable, Slot.Tablet, Slot.Upgrade,
                "eeprom",  "tool" };

        sloticons = new HashMap<>();
        for(String type : SLOT_TYPES)
            SlotIcons.sloticons.put(type, new ResourceLocation("opencomputers", "textures/icons/"+type+".png"));
    }

    private static void registerTierIcons(){
        if(tiericons != null) return;

        tiericons = new HashMap<>();
        tiericons.put(-1, new ResourceLocation("opencomputers", "textures/icons/na.png"));
        tiericons.put( 0, new ResourceLocation("opencomputers", "textures/icons/tier0.png"));
        tiericons.put( 1, new ResourceLocation("opencomputers", "textures/icons/tier1.png"));
        tiericons.put( 2, new ResourceLocation("opencomputers", "textures/icons/tier2.png"));
    }

    public static ResourceLocation fromTier(int tier){
        SlotIcons.registerTierIcons();

        if(tiericons.containsKey(tier))
            return tiericons.get(tier);

        return null;
    }

    public static ResourceLocation fromSlot(String slot){
        SlotIcons.registerSlotTypeIcons();

        if(sloticons.containsKey(slot))
            return sloticons.get(slot);

        return null;
    }
}