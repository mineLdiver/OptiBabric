package me.modmuss50.optifabric.patcher.metadata;

import net.fabricmc.loader.api.metadata.ModOrigin;

interface OptifineOrigin extends ModOrigin {
    @Override
    default Kind getKind() {
        return Kind.PATH;
    }

    @Override
    default String getParentModId() {
        throw new UnsupportedOperationException("kind " + Kind.PATH.name() + " doesn't have a parent mod");
    }

    @Override
    default String getParentSubLocation() {
        throw new UnsupportedOperationException("kind " + Kind.PATH.name() + " doesn't have a parent sub-location");
    }
}
