package no.ks.fiks.io.klient;

import org.eclipse.jetty.client.api.Request;

public interface AuthenticationStrategy {
    void setAuthenticationHeaders(Request request);
}
