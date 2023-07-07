package me.modmuss50.optifabric.patcher.metadata;

import net.fabricmc.loader.api.metadata.ModOrigin;

interface OptifineOrigin extends ModOrigin {
    @Override
    default Kind getKind() {
        return Kind.UNKNOWN;
    }

    @Override
    default String getParentModId() {
        return "optifabric";
    }

    @Override
    default String getParentSubLocation() {
        throw new UnsupportedOperationException("kind " + getKind().name() + " doesn't have a parent sub-location");
    }
}
