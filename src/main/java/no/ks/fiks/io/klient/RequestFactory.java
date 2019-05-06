package no.ks.fiks.io.klient;

import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;

public interface RequestFactory {
    Request createSendToFiksIORequest(ContentProvider contentProvider);
}
