package me.modmuss50.optifabric.compat.modmenu;

import me.modmuss50.optifabric.api.entrypoint.CompatProvider;

import java.util.Set;

public class ModMenuCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "modmenu";
    }

    @Override
    public boolean isSupportedVersion() {
        return true;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of("optifabric.compat.modmenu.mixins.json");
    }
}
