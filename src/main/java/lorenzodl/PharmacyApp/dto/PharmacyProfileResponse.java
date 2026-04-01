package lorenzodl.PharmacyApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PharmacyProfileResponse {

    private String nomeFarmacia;
    private String indirizzo;
    private String telefono;
    private String partitaIva;
    private String orariApertura;
    private String imageUrl;
    private String email;
    private String nomeReferente;
    private String cognomeReferente;
}