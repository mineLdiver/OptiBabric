package me.modmuss50.optifabric.compat.glsl;

import me.modmuss50.optifabric.api.OptiFineInfo;
import me.modmuss50.optifabric.api.entrypoint.CompatProvider;
import me.modmuss50.optifabric.api.util.Edition;

import java.util.Set;

public class GLSLCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "glsl";
    }

    @Override
    public boolean isSupportedVersion() {
        return OptiFineInfo.getEdition() != Edition.ANTIALIASING;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of("optifabric.compat.glsl.mixins.json");
    }
}
