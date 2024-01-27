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
        DataSourceBuilder builder = DataSourceBuilder.create();
 
        // Use the DATABASE_URL environment variable, if it exists
        String databaseURL = System.getenv("DATABASE_URL");
        if (databaseURL != null) {
            String jdbcURL = getSpringDataSourceURL(databaseURL);
            builder.url(jdbcURL);
        }
        return builder.build();
    }
 
    private String getSpringDataSourceURL(String databaseURL) throws URISyntaxException {
        URI uri = new URI(databaseURL);
        String username = "";
        String password = "";
        String userInfo = uri.getUserInfo();
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
        if (!username.equals("")) {
            jdbcURL = jdbcURL + "?user=" + username;
            if (!password.equals("")) {
                jdbcURL = jdbcURL + "&password=" + password;
            }
        }
        return jdbcURL;
    }
}
