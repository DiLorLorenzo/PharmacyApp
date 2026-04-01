package lorenzodl.PharmacyApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PharmacyOrderResponse {

    private Long id;
    private String customerName;
    private String customerEmail;
    private LocalDateTime createdAt;
    private String status;
    private Double totalAmount;
    private String prescriptionUrl;
    private List<PharmacyOrderItemResponse> items;
}