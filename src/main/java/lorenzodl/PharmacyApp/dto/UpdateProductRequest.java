package lorenzodl.PharmacyApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    @NotBlank
    private String nome;

    private String descrizione;

    @NotNull
    private Double prezzo;

    @NotNull
    private Integer quantity;

    private String imageUrl;

    private boolean requiresPrescription;
}