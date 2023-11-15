package io.github.shymega.repaircafe.api.ext.tito;

public class TitoAdminApiClient extends TitoApiClient {
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
