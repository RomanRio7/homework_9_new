package cinescope.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class DbUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String CREDS_FILE = "/db_credentials_cinescope.json";

    public static DbCredentials loadCredentials() {
        try (InputStream is = DbUtils.class.getResourceAsStream(CREDS_FILE)) {
            if (is == null) {
                throw new IllegalStateException("db_credentials_cinescope.json not found in resources");
            }
            return MAPPER.readValue(is, DbCredentials.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read DB credentials", e);
        }
    }
}