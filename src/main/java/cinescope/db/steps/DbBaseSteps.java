package cinescope.db.steps;

import cinescope.util.DbCredentials;
import cinescope.util.DbUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public abstract class DbBaseSteps {

    protected final Jdbi jdbi;

    protected DbBaseSteps() {
        DbCredentials creds = DbUtils.loadCredentials();

        String url = String.format(
                "jdbc:postgresql://%s:%d/%s",
                creds.getHost(),
                creds.getPort(),
                creds.getDbName()
        );

        this.jdbi = Jdbi.create(url, creds.getUsername(), creds.getPassword())
                .installPlugin(new PostgresPlugin())
                .installPlugin(new SqlObjectPlugin())
                .installPlugin(new Jackson2Plugin());

        configureMapper();
    }

    private void configureMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        jdbi.getConfig(Jackson2Config.class).setMapper(mapper);
    }
}