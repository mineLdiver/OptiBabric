package me.modmuss50.optifabric.compat.macula;

import me.modmuss50.optifabric.api.OptiFineInfo;
import me.modmuss50.optifabric.api.entrypoint.CompatProvider;
import me.modmuss50.optifabric.api.util.Edition;

import java.util.Set;

public class MaculaCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "macula";
    }

    @Override
    public boolean isSupportedVersion() {
        return OptiFineInfo.getEdition() != Edition.ANTIALIASING;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of(
                "optifabric.compat.macula.mixins.json",
                "optifabric.compat.macula.%s.mixins.json".formatted(OptiFineInfo.getEdition().name().toLowerCase())
        );
    }
}
