package no.ks.fiks.io.klient;

import lombok.Builder;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;

@Builder
public class RequestFactoryImpl implements RequestFactory {
    static final String BASE_PATH = "/fiks-io/api/v1/";
    private final HttpClient client;
    private final String scheme;
    private final String hostName;
    private final Integer portNumber;

    @Override
    public Request createSendToFiksIORequest(ContentProvider contentProvider) {
        return client.newRequest(hostName, portNumber)
                                .scheme(scheme)
                                .method(HttpMethod.POST)
                                .path(BASE_PATH + "send")
                                .content(contentProvider);
    }
}
