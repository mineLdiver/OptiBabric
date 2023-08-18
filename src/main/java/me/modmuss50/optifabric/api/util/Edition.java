package me.modmuss50.optifabric.api.util;

import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Edition {
    STANDARD("HD", "Standard"),
    SMOOTH("HD_S", "Smooth"),
    MULTITHREADED("HD_MT", "Multi-Core"),
    ANTIALIASING("HD_AA", "AA");

    public final String id;
    public final String canonName;

    Edition(String id, String canonName) {
        this.id = id;
        this.canonName = canonName;
    }

    public record EditionAndVersion(Edition edition, String version) {}

    public static EditionAndVersion fromVersion(String version) {
        String editionAndVersion = version.substring("OptiFine_1.7.3_".length());
        Set<Edition> matches = Arrays.stream(values()).filter(edition -> editionAndVersion.startsWith(edition.id)).collect(Collectors.toCollection(() -> EnumSet.noneOf(Edition.class)));
        if (matches.size() == 0) throw new IllegalStateException("Unknown OptiFine edition: " + version);
        if (matches.size() > 1) matches.remove(STANDARD);
        Edition edition = Iterables.getOnlyElement(matches);
        return new EditionAndVersion(edition, editionAndVersion.substring(edition.id.length() + 1));
    }

    @Override
    public String toString() {
        return id;
    }
}