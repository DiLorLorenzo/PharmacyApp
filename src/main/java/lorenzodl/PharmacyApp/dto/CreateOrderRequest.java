package lorenzodl.PharmacyApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull
    private Long pharmacyId;

    @NotNull
    private List<OrderItemRequest> items;
}