package lorenzodl.PharmacyApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePayPalOrderRequest {
    private Double total;
}