package no.ks.fiks.svarinn2.klient;

import org.eclipse.jetty.client.api.Request;

public interface AuthenticationStrategy {
    void setAuthenticationHeaders(Request request);
}
