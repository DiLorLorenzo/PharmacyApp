package lorenzodl.PharmacyApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhoneRequest {

    @NotBlank
    private String phoneNumber;
}