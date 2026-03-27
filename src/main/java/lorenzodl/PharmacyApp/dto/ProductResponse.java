package lorenzodl.PharmacyApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private Double prezzo;
    private Integer quantity;
    private String imageUrl;
    private boolean requiresPrescription;
}