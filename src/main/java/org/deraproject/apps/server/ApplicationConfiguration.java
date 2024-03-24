package org.deraproject.apps.server;

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

    @Value("${DATABASE_URL:#{null}}")
    private String DATABASE_URL;

    @Value("${spring.datasource.url:#{null}}")
    private String springDatasourceURL;

    @Bean
    public DataSource getDataSource() throws URISyntaxException {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();

        // Use the DATABASE_URL environment variable, if it exists
        try {
            if (DATABASE_URL != null || springDatasourceURL == null) {
                return builder.url(getSpringDataSourceURL(DATABASE_URL)).build();
            } else {
                return builder.url(springDatasourceURL).build();
            }
        } catch (final NullPointerException ex) {
                return builder.url("jdbc:h2:mem:dera_server_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH").build();
        }
    }

    private final String getSpringDataSourceURL(final String databaseURL) throws URISyntaxException {
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
        jdbcURL = jdbcURL + "&" + uri.getQuery();

        System.out.println(databaseURL);
        System.out.println(jdbcURL);

        return jdbcURL;
    }
}
