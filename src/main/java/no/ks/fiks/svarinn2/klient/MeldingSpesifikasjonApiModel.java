package no.ks.fiks.svarinn2.klient;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@Builder
public class MeldingSpesifikasjonApiModel {
    @NotNull private UUID avsenderKontoId;
    @NotNull private UUID mottakerKontoId;
    @NotNull private String meldingType;
    private UUID svarPaMelding;
    private Long ttl;
}
