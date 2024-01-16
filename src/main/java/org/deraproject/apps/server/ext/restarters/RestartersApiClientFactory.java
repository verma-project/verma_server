package org.deraproject.apps.server.ext.restarters;

import lombok.Getter;
import org.deraproject.apps.server.ext.tito.TitoApiClientFactory;
import org.deraproject.apps.server.utils.bases.BaseRestApiClient;
import org.springframework.web.reactive.function.client.WebClient;

public final class RestartersApiClientFactory {
    public RestartersApiClient getClient(final RestartersApiClientType clientType, final String apiToken) {
        if (clientType == null) throw new IllegalArgumentException("Client type variant is null. Pass a valid variant!");
        if (apiToken == null || apiToken.isEmpty()) throw new IllegalArgumentException("Empty/null API token for Restarters supplied. Pass a valid token.");

        return switch (clientType) {
            case FIXOMETER -> new RestartersFixometerApiClient(apiToken);
        };
    }

    public enum RestartersApiClientType {
        FIXOMETER,
    }

    private static final class RestartersFixometerApiClient extends RestartersApiClient {
        private final String RESTARTERS_FIXOMETER_API_BASE_URL = "";

        public RestartersFixometerApiClient(final String apiToken) {
            try {
                configureCredentials(apiToken);
                configureBaseUrl(RESTARTERS_FIXOMETER_API_BASE_URL);;
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); // handle
            }
        }
    }

    /*
     * We use an abstract class, because Interfaces can't have protected fields, which we need.
     */
    public static abstract class RestartersApiClient extends BaseRestApiClient {

        protected WebClient CLIENT = null;
        private String API_TOKEN = null;
        private String BASE_URL = null;

        void configureCredentials(final String apiToken) throws IllegalArgumentException {
            if (apiToken == null || apiToken.isEmpty()) throw new IllegalArgumentException("No API token provided!");
            if (API_TOKEN == null || API_TOKEN.isEmpty()) API_TOKEN = apiToken;
            if (CLIENT == null) CLIENT = super.webClient
                .mutate()
                .defaultHeader("Authorization", "Token token=" + API_TOKEN)
                .build();
        }

        void configureBaseUrl(final String baseUrl) {
            if (baseUrl == null || baseUrl.isEmpty()) throw new IllegalArgumentException("No base URL provided!");
            if (BASE_URL == null || BASE_URL.isEmpty()) BASE_URL = baseUrl;
            if (CLIENT == null)
                throw new IllegalArgumentException("Client field is null, configure with appropriate method first!");

            CLIENT.mutate()
                .baseUrl(baseUrl);
        }
    }
}
