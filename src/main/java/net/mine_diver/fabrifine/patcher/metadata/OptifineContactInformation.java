package net.mine_diver.fabrifine.patcher.metadata;

import net.fabricmc.loader.api.metadata.ContactInformation;

import java.util.Map;
import java.util.Optional;

class OptifineContactInformation implements ContactInformation {
    private static final Map<String, String> OPTIFINE_CONTACTS = Map.of(
            "email", "optifinex@gmail.com",
            "homepage", "https://optifine.net",
            "issues", "https://github.com/sp614x/optifine/issues",
            "sources", "https://github.com/sp614x/optifine"
    );
    static final OptifineContactInformation INSTANCE = new OptifineContactInformation();

    private OptifineContactInformation() {}

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(OPTIFINE_CONTACTS.get(key));
    }

    @Override
    public Map<String, String> asMap() {
        return OPTIFINE_CONTACTS;
    }
}
