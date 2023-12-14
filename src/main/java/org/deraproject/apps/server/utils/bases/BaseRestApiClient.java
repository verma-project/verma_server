package org.deraproject.apps.server.utils.bases;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;
import java.time.Duration;

public class BaseRestApiClient {
    private final HttpClient httpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .connectTimeout(Duration.ofSeconds(30))
        .build();

    private final ClientHttpConnector connector = new JdkClientHttpConnector(httpClient);
    protected final WebClient webClient = WebClient.builder()
        .clientConnector(connector)
        .defaultHeader("Accept", "application/json")
        .build();
}
