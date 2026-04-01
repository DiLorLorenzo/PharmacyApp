package lorenzodl.PharmacyApp.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PharmacyOrderItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
}