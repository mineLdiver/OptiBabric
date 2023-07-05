package net.mine_diver.fabrifine.patcher.metadata;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.*;
import net.mine_diver.fabrifine.FabriFine;
import net.mine_diver.fabrifine.util.Edition;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class OptifineMetadata implements ModMetadata {
    private final Version version;

    OptifineMetadata(Version version) {
        this.version = version;
    }

    @Override
    public String getType() {
        return "mcp";
    }

    @Override
    public String getId() {
        return "optifine";
    }

    @Override
    public Collection<String> getProvides() {
        return Set.of();
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public ModEnvironment getEnvironment() {
        return ModEnvironment.CLIENT;
    }

    @Override
    public Collection<ModDependency> getDependencies() {
        return Set.of();
    }

    @Override
    public String getName() {
        return FabriFine.EDITION == Edition.STANDARD ? "OptiFine" : "OptiFine " + FabriFine.EDITION.canonName;
    }

    @Override
    public String getDescription() {
        return "OptiFine is a Minecraft optimization mod. It allows Minecraft to run faster and look better with full support for shaders, HD textures and many configuration options.";
    }

    @Override
    public Collection<Person> getAuthors() {
        return Set.of(OptifinePerson.INSTANCE);
    }

    @Override
    public Collection<Person> getContributors() {
        return Set.of(OptifinePerson.INSTANCE);
    }

    @Override
    public ContactInformation getContact() {
        return OptifineContactInformation.INSTANCE;
    }

    @Override
    public Collection<String> getLicense() {
        return Set.of("All rights reserved");
    }

    @Override
    public Optional<String> getIconPath(int size) {
        return Optional.of("assets/optifine/icon.png");
    }

    @Override
    public boolean containsCustomValue(String key) {
        return false;
    }

    @Override
    public CustomValue getCustomValue(String key) {
        return null;
    }

    @Override
    public Map<String, CustomValue> getCustomValues() {
        return Map.of();
    }

    @Override
    public boolean containsCustomElement(String key) {
        return false;
    }
}
