package me.modmuss50.optifabric.compat.bnb;

import me.modmuss50.optifabric.api.entrypoint.CompatProvider;

import java.util.Set;

public class BetterNetherBetaCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "bnb";
    }

    @Override
    public boolean isSupportedVersion() {
        return true;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of("optifabric.compat.bnb.mixins.json");
    }
}
