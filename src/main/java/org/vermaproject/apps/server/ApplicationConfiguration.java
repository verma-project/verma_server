package org.vermaproject.apps.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class ApplicationConfiguration {
    @Value("${DATABASE_URL:#{null}}")
    private String DATABASE_URL;

    @Value("${spring.datasource.url:#{null}}")
    private String SPRING_DATASOURCE_URL;

    private final String DEFAULT_DB_URL = "jdbc:h2:mem:verma_server;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH";

    @Bean
    public DataSource getDataSource() throws URISyntaxException {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();

        // TODO: Document the following code snippet.
        // First try `DATABASE_URL` and then `SPRING_DATASOURCE_URL.
        if (DATABASE_URL != null) {
            log.info("Using `$DATABASE_URL` for DB connection: {}", DATABASE_URL);
            return builder.url(getSpringDataSourceURL(DATABASE_URL)).build();
             // Now try `SPRING_DATASOURCE_URL`.
        } else if (SPRING_DATASOURCE_URL != null) {
            log.info("Using `$SPRING_DATASOURCE_URL` for DB connection: {}", SPRING_DATASOURCE_URL);
            return builder.url(SPRING_DATASOURCE_URL).build();
            // Finally, fallback to H2 in-mem DB.
        } else {
            log.info("Using default H2 in-mem DB connection: {}", DEFAULT_DB_URL);
            return builder.url(DEFAULT_DB_URL).build();
        }
    }

    private String getSpringDataSourceURL(final String databaseURL) throws URISyntaxException {
        final URI uri = new URI(databaseURL);
        String username = "";
        String password = "";
        final String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            String[] components = userInfo.split(":");
            username = components[0];
            if (components.length == 2) {
                password = components[1];
            }
        }
        final String host = uri.getHost();
        final int port = uri.getPort();
        final String path = uri.getPath();
        String scheme = uri.getScheme();
        final String query = uri.getQuery();

        if ("postgres".equals(scheme)) {
            scheme = "postgresql";
        }
        String jdbcURL = "jdbc:" + scheme + "://" + host + ":" + port + path;
        if (!username.isEmpty()) {
            jdbcURL = jdbcURL + "?user=" + username;
            if (!password.isEmpty()) {
                jdbcURL = jdbcURL + "&password=" + password;
            }
        }
        if (!query.isEmpty()) {
            jdbcURL = jdbcURL + "&" + uri.getQuery();
        }

        return jdbcURL;
    }
}
