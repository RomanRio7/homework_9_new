package cinescope.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String getUserId(String jwt) {
        try {
            String payload = jwt.split("\\.")[1];
            String json = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            JsonNode node = MAPPER.readTree(json);
            return node.get("id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse JWT and extract userId", e);
        }
    }
}