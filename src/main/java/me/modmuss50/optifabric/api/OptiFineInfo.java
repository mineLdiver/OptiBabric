package me.modmuss50.optifabric.api;

import me.modmuss50.optifabric.api.util.Edition;
import me.modmuss50.optifabric.mod.OptifabricSetup;

import java.util.Objects;

public final class OptiFineInfo {
    public static Edition getEdition() {
        return Objects.requireNonNull(OptifabricSetup.EDITION);
    }

    public static String getVersion() {
        return Objects.requireNonNull(OptifabricSetup.VERSION);
    }

    private OptiFineInfo() {}
}
