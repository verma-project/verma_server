package org.deraproject.apps.server;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public DataSource getDataSource() throws URISyntaxException {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();

        // Use the DATABASE_URL environment variable, if it exists
        final String databaseURL = System.getenv("DATABASE_URL");
        if (databaseURL != null) builder.url(getSpringDataSourceURL(databaseURL));

        builder.url("jdbc:h2:mem:dera_db");

        return builder.build();
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
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();
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
        return jdbcURL;
    }
}
