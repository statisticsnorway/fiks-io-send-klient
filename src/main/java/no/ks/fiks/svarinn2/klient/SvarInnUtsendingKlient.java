package no.ks.fiks.svarinn2.klient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.client.util.InputStreamResponseListener;
import org.eclipse.jetty.client.util.MultiPartContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static org.eclipse.jetty.http.HttpStatus.isClientError;
import static org.eclipse.jetty.http.HttpStatus.isServerError;

@Slf4j
public class SvarInnUtsendingKlient {

    private final HttpClient client = new HttpClient();
    private final String svarInnScheme;
    private final String svarInnHost;
    private final Integer svarInnPort;
    private final AuthenticationStrategy authenticationStrategy;
    private Function<Request, Request> requestInterceptor;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_PATH = "/svarinn2/api/v1/";

    public SvarInnUtsendingKlient(@NonNull String svarInnScheme, @NonNull String svarInnHost, @NonNull Integer svarInnPort, @NonNull AuthenticationStrategy authenticationStrategy, @NonNull Function<Request, Request> requestInterceptor) {
        this.svarInnScheme = svarInnScheme;
        this.svarInnHost = svarInnHost;
        this.svarInnPort = svarInnPort;
        this.authenticationStrategy = authenticationStrategy;
        this.requestInterceptor = requestInterceptor;

        try {
            this.client.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SvarInnUtsendingKlient(@NonNull String svarInnScheme, @NonNull String svarInnHost, @NonNull Integer svarInnPort, @NonNull AuthenticationStrategy authenticationStrategy) {
        this(svarInnScheme, svarInnHost, svarInnPort, authenticationStrategy, r -> r);
    }

    public SendtMeldingApiModel send(@NonNull MeldingSpesifikasjonApiModel metadata, @NonNull Option<InputStream> data) {
        MultiPartContentProvider contentProvider = new MultiPartContentProvider();
        contentProvider.addFieldPart("metadata", new StringContentProvider("application/json", serialiser(metadata), Charset.forName("UTF-8")), null);
        if (data.isDefined())
            contentProvider.addFilePart("data", UUID.randomUUID().toString(), new InputStreamContentProvider(data.get()), null);

        contentProvider.close();

        InputStreamResponseListener listener = new InputStreamResponseListener();
        Request request = client.newRequest(svarInnHost, svarInnPort)
                .scheme(svarInnScheme)
                .method(HttpMethod.POST)
                .path(BASE_PATH + "send")
                .content(contentProvider);

        authenticationStrategy.setAuthenticationHeaders(request);

        requestInterceptor.apply(request).send(listener);

        try {
            Response response = listener.get(1, TimeUnit.HOURS);
            if (isClientError(response.getStatus()) || isServerError(response.getStatus())) {
                int status = response.getStatus();
                String content = IOUtils.toString(listener.getInputStream(), StandardCharsets.UTF_8);
                throw new SvarInnHttpException(String.format("HTTP-feil under sending av melding (%d): %s", status, content), status, content);
            }
            return objectMapper.readValue(listener.getInputStream(), SendtMeldingApiModel.class);
        } catch (InterruptedException | TimeoutException | ExecutionException | IOException e) {
            throw new RuntimeException("Feil under invokering av svarinn api", e);
        }
    }

    private String serialiser(@NonNull Object metadata) {
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Feil under serialisering av metadata", e);
        }
    }
}
