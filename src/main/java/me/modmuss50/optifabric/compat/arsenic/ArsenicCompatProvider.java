package me.modmuss50.optifabric.compat.arsenic;

import me.modmuss50.optifabric.api.OptiFineInfo;
import me.modmuss50.optifabric.api.entrypoint.CompatProvider;
import me.modmuss50.optifabric.api.util.Edition;

import java.util.Set;

public class ArsenicCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "station-renderer-arsenic";
    }

    @Override
    public boolean isSupportedVersion() {
        return OptiFineInfo.getEdition() != Edition.ANTIALIASING;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of("optifabric.compat.arsenic.mixins.json");
    }
}
