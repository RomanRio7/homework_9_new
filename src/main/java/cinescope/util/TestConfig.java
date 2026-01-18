package cinescope.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = TestConfig.class.getResourceAsStream("/test.properties")) {
            if (is != null) {
                PROPS.load(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load test.properties", e);
        }
    }

    private TestConfig() {}

    public static String get(String key) {
        String fromSys = System.getProperty(key);
        if (fromSys != null && !fromSys.isBlank()) return fromSys;

        String fromEnv = System.getenv(key.replace('.', '_').toUpperCase());
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;

        String fromProps = PROPS.getProperty(key);
        if (fromProps == null || fromProps.isBlank()) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        return fromProps;
    }
}