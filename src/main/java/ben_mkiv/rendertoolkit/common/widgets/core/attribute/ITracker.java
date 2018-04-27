package ben_mkiv.rendertoolkit.common.widgets.core.attribute;

import ben_mkiv.rendertoolkit.common.widgets.component.world.EntityTracker3D;

import java.util.UUID;

public interface ITracker extends IAttribute {
    void setupTracking(EntityTracker3D.EntityType trackingType, int range);
    void setupTrackingFilter(String type, int metaindex);
    void setupTrackingEntity(UUID uuid);
}

