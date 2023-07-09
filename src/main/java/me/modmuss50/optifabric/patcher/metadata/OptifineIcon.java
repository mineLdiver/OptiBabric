package me.modmuss50.optifabric.patcher.metadata;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class OptifineIcon {
    public static final String DATA;
    static {
        try {
            DATA = IOUtils.toString(Objects.requireNonNull(OptifineIcon.class.getResourceAsStream("/assets/optifabric/optifine_icon")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private OptifineIcon() {
        throw new UnsupportedOperationException();
    }
}
