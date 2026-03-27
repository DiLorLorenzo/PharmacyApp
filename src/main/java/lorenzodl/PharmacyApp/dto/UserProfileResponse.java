package lorenzodl.PharmacyApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String phoneNumber;
    private String role;
}