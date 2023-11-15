package io.github.shymega.repaircafe.api.ext.tito;

public class TitoCheckinApiClient extends TitoApiClient {
    private final String TITO_CHECKIN_BASE_URL = "https://checkin.tito.io/";

    public TitoCheckinApiClient(String apiToken) {
        try {
            configureCredentials(apiToken);
            configureBaseUrl(TITO_CHECKIN_BASE_URL);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
