package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

import java.util.UUID;

public interface IPrivate extends IAttribute{
	UUID setOwner(String playerUUID);
	String getOwner();
	UUID getOwnerUUID();
}
