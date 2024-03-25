package org.vermaproject.apps.server;

import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class ApplicationConfiguration {
    @Value("${environment.DATABASE_URL:#{null}}")
    private String DATABASE_URL;

    @Value("${spring.datasource.url:#{null}}")
    private String DATASOURCE_URL_PROP;

    @Bean
    public DataSource getDataSource() throws URISyntaxException {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();

        // TODO: Document the following code snippet.
        try {
            if (DATABASE_URL != null && DATASOURCE_URL_PROP == null) {
                return builder.url(getSpringDataSourceURL(DATABASE_URL)).build();
            } else if (DATABASE_URL == null && DATASOURCE_URL_PROP != null) {
                return builder.url(DATASOURCE_URL_PROP).build();
            } else {
                return builder.url("jdbc:h2:mem:verma_server;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH").build();
            }
        } catch (final NullPointerException e) {
                return builder.url("jdbc:h2:mem:verma_server;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH").build();
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
