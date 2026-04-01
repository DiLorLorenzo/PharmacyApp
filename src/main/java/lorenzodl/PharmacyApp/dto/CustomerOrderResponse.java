package lorenzodl.PharmacyApp.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomerOrderResponse {

    private Long id;
    private String pharmacyName;
    private LocalDateTime createdAt;
    private String status;
    private Double totalAmount;
    private String prescriptionUrl;
    private List<CustomerOrderItemResponse> items;
}