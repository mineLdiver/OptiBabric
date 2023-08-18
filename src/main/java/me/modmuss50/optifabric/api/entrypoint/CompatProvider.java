package me.modmuss50.optifabric.api.entrypoint;

import java.util.Set;

@FunctionalInterface
public interface CompatProvider {
    String modid();
    default boolean isSupportedVersion() {
        return false;
    }
    default Set<String> getMixinConfigs() {
        return Set.of();
    }
}
