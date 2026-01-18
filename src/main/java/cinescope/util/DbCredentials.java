package cinescope.util;

import lombok.Data;

@Data
public class DbCredentials {
    private String host;
    private int port;
    private String dbName;
    private String username;
    private String password;
}