package io.github.shymega.repaircafe.api.ext.tito;

public class TitoApiClientFactory {
    public TitoApiClient getClient(TitoApiClientType clientType, String apiToken) throws IllegalArgumentException {
        if (clientType == null) throw new IllegalArgumentException("Client type variant is NULL!");

        return switch (clientType) {
            case ADMIN -> new TitoAdminApiClient(apiToken);
            case PUBLIC -> new TitoCheckinApiClient(apiToken);
        };
    }

    public enum TitoApiClientType {
        ADMIN,
        PUBLIC,
    }
}
