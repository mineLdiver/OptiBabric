package net.mine_diver.fabrifine.util;

import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Edition {
    STANDARD("HD"),
    SMOOTH("HD_S"),
    MULTITHREADED("HD_MT"),
    ANTIALIASING("HD_AA");

    public final String id;

    public static Edition fromVersion(String version) {
        String editionAndVersion = version.substring("OptiFine_1.7.3_".length());
        Set<Edition> matches = Arrays.stream(values()).filter(edition -> editionAndVersion.startsWith(edition.id)).collect(Collectors.toCollection(() -> EnumSet.noneOf(Edition.class)));
        if (matches.size() == 0) throw new IllegalStateException("Unknown OptiFine edition: " + version);
        if (matches.size() > 1) matches.remove(STANDARD);
        return Iterables.getOnlyElement(matches);
    }

    @Override
    public String toString() {
        return id;
    }
}