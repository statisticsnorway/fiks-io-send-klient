package no.ks.fiks.io.klient;

public class FiksIOHttpException extends RuntimeException {

    private final int status;
    private final String response;

    FiksIOHttpException(String message, int status, String response) {
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
