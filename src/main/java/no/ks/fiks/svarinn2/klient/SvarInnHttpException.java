package no.ks.fiks.svarinn2.klient;

public class SvarInnHttpException extends RuntimeException {

    private final int status;
    private final String response;

    SvarInnHttpException(String message, int status, String response) {
        super(message);
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }
}
