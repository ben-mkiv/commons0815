package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IEntity extends IAttribute {
    boolean setEntity(EntityLivingBase entity);
    boolean setEntity(NBTTagCompound entityNBT, World world);
}