package me.modmuss50.optifabric.compat.fov;

import me.modmuss50.optifabric.api.entrypoint.CompatProvider;

import java.util.Set;

public class FovCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "fov";
    }

    @Override
    public boolean isSupportedVersion() {
        return true;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of("optifabric.compat.fov.mixins.json");
    }
}
