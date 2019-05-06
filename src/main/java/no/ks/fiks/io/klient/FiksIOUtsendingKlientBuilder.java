package no.ks.fiks.io.klient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;

import java.util.function.Function;

/**
 * Builder that must be used to create
 */
public class FiksIOUtsendingKlientBuilder {

    private HttpClient httpClient = new HttpClient();

    private String scheme = "https";

    private String hostName;

    private Integer portNumber;

    private AuthenticationStrategy authenticationStrategy;

    private Function<Request, Request> requestInterceptor;

    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public FiksIOUtsendingKlientBuilder withHttpClient(@NonNull final HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public FiksIOUtsendingKlientBuilder withScheme(@NonNull final String scheme) {
        this.scheme = scheme;
        return this;
    }

    public FiksIOUtsendingKlientBuilder withHostName(@NonNull final String hostName) {
        this.hostName = hostName;
        return this;
    }

    public FiksIOUtsendingKlientBuilder withPortNumber(@NonNull final Integer portNumber) {
        this.portNumber = portNumber;
        return this;
    }

    public FiksIOUtsendingKlientBuilder withAuthenticationStrategy(@NonNull final AuthenticationStrategy authenticationStrategy) {
        this.authenticationStrategy = authenticationStrategy;
        return this;
    }

    public FiksIOUtsendingKlientBuilder withRequestInterceptor(Function<Request, Request> requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
        return this;
    }

    public FiksIOUtsendingKlientBuilder withObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public FiksIOUtsendingKlient build() {
        objectMapper = new ObjectMapper();
        return new FiksIOUtsendingKlient(
                createRequestFactory(),
                authenticationStrategy,
                getOrCreateRequestInterceptor(),
                getOrCreateObjectMapper()
        );
    }

    private RequestFactory createRequestFactory() {
        return RequestFactoryImpl.builder()
                                 .client(httpClient)
                                 .scheme(scheme)
                                 .hostName(hostName)
                                 .portNumber(portNumber)
                                 .build();
    }

    private Function<Request, Request> getOrCreateRequestInterceptor() {
        return requestInterceptor == null ? request -> request : requestInterceptor;
    }

    private ObjectMapper getOrCreateObjectMapper() {
        return objectMapper == null ? new ObjectMapper().findAndRegisterModules() : objectMapper;
    }
}
