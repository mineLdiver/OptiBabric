package me.modmuss50.optifabric.compat.stationflattening;

import me.modmuss50.optifabric.api.entrypoint.CompatProvider;

import java.util.Set;

public class StationFlatteningCompatProvider implements CompatProvider {
    @Override
    public String modid() {
        return "station-flattening-v0";
    }

    @Override
    public boolean isSupportedVersion() {
        return true;
    }

    @Override
    public Set<String> getMixinConfigs() {
        return Set.of("optifabric.compat.station-flattening-v0.mixins.json");
    }
}
