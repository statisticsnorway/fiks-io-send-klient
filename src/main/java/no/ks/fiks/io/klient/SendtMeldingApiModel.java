package no.ks.fiks.io.klient;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class SendtMeldingApiModel {
    @NonNull private UUID meldingId;
    @NonNull private String meldingType;
    @NonNull private UUID avsenderKontoId;
    @NonNull private UUID mottakerKontoId;
    @NonNull private Long ttl;
    private UUID dokumentlagerId;
    private UUID svarPaMelding;
    private Map<String, String> headere;
}