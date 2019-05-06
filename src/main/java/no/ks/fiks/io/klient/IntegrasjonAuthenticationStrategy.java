package no.ks.fiks.io.klient;

import no.ks.fiks.maskinporten.Maskinportenklient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;

import java.util.UUID;

public class IntegrasjonAuthenticationStrategy implements AuthenticationStrategy {

    static final String INTEGRASJON_ID = "IntegrasjonId";

    static final String INTEGRASJON_PASSWORD = "IntegrasjonPassord";

    private final Maskinportenklient maskinportenklient;
    private final UUID integrasjonId;
    private final String integrasjonPassord;

    public IntegrasjonAuthenticationStrategy(Maskinportenklient maskinportenklient, UUID integrasjonId, String integrasjonPassord) {
        this.maskinportenklient = maskinportenklient;
        this.integrasjonId = integrasjonId;
        this.integrasjonPassord = integrasjonPassord;
    }

    @Override
    public void setAuthenticationHeaders(Request request) {
        request.header(HttpHeader.AUTHORIZATION, "Bearer " + getAccessToken())
                .header(INTEGRASJON_ID, integrasjonId.toString())
                .header(INTEGRASJON_PASSWORD, integrasjonPassord);
    }

    private String getAccessToken() {
        return maskinportenklient.getAccessToken("ks");
    }
}
