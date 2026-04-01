package lorenzodl.PharmacyApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PublicPharmacyResponse {

    private Long id;
    private String nome;
    private String indirizzo;
    private String telefono;
    private String orariApertura;
    private String imageUrl;
}