package lorenzodl.PharmacyApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPharmacyRequest {

    @NotBlank
    private String nomeFarmacia;

    @NotBlank
    private String indirizzo;

    @NotBlank
    private String telefono;

    @NotBlank
    private String partitaIva;

    private String orariApertura;

    private String imageUrl;

    @NotBlank
    private String nomeReferente;

    @NotBlank
    private String cognomeReferente;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}