package io.github.shymega.repaircafe.api.ext.tito;

import io.github.shymega.repaircafe.api.utils.bases.BaseRestApiClient;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
public abstract class TitoApiClient extends BaseRestApiClient {
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
