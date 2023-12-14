package org.deraproject.apps.server.ext.tito;

import org.deraproject.apps.server.utils.bases.BaseRestApiClient;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

public final class TitoApiClientFactory {
    public TitoApiClient getClient(TitoApiClientType clientType, String apiToken) throws IllegalArgumentException {
        if (clientType == null) throw new IllegalArgumentException("Client type variant is null. Pass a valid variant!");
        if (apiToken == null || apiToken.isEmpty()) throw new IllegalArgumentException("Empty/null API token for Ti.to supplied. Pass a valid token.");

        return switch (clientType) {
            case ADMIN -> new TitoAdminApiClient(apiToken);
            case PUBLIC -> new TitoCheckinApiClient(apiToken);
        };
    }

    public enum TitoApiClientType {
        ADMIN,
        PUBLIC,
    }

    private static final class TitoAdminApiClient extends TitoApiClient {
        private final String TITO_ADMIN_API_BASE_URL = "https://api.tito.io/v3/";

        public TitoAdminApiClient(String apiToken) {
            try {
                configureCredentials(apiToken);
                configureBaseUrl(TITO_ADMIN_API_BASE_URL);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private static final class TitoCheckinApiClient extends TitoApiClient {
        private final String TITO_CHECKIN_BASE_URL = "https://checkin.tito.io/";

        public TitoCheckinApiClient(String apiToken) {
            try {
                if (apiToken == null || apiToken.isEmpty()) throw new IllegalArgumentException("Empty/null API token for the Ti.to Check-in API supplied");
                configureCredentials(apiToken);
                configureBaseUrl(TITO_CHECKIN_BASE_URL);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Getter
    public static abstract class TitoApiClient extends BaseRestApiClient {
        protected WebClient CLIENT = null;
        private String API_TOKEN = null;
        private String BASE_URL = null;

        void configureCredentials(String apiToken) throws IllegalArgumentException {
            if (apiToken == null || apiToken.isEmpty()) throw new IllegalArgumentException("No API token provided!");
            if (API_TOKEN == null || API_TOKEN.isEmpty()) API_TOKEN = apiToken;
            if (CLIENT == null) CLIENT = super.webClient
                .mutate()
                .defaultHeader("Authorization", "Token token=" + API_TOKEN)
                .build();
        }

        void configureBaseUrl(String baseUrl) {
            if (baseUrl == null || baseUrl.isEmpty()) throw new IllegalArgumentException("No base URL provided!");
            if (BASE_URL == null || BASE_URL.isEmpty()) BASE_URL = baseUrl;
            if (CLIENT == null)
                throw new IllegalArgumentException("Client field is null, configure with appropriate method first!");

            CLIENT.mutate()
                .baseUrl(baseUrl);
        }
    }
}
