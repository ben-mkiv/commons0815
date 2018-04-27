package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItem extends IAttribute{
	boolean setItem(ItemStack itm);
	boolean setItem(String itm, int meta);
	Item getItem();
}
