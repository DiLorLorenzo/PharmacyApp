package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.CustomerOrderItemResponse;
import lorenzodl.PharmacyApp.dto.CustomerOrderResponse;
import lorenzodl.PharmacyApp.entities.OrderItem;
import lorenzodl.PharmacyApp.entities.PurchaseOrder;
import lorenzodl.PharmacyApp.entities.User;
import lorenzodl.PharmacyApp.repository.PurchaseOrderRepository;
import lorenzodl.PharmacyApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final UserRepository userRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public List<CustomerOrderResponse> getMyOrders(String email) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return purchaseOrderRepository.findByCustomerId(customer.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CustomerOrderResponse mapToResponse(PurchaseOrder order) {
        List<CustomerOrderItemResponse> items = order.getItems().stream()
                .map(this::mapItemToResponse)
                .toList();

        return new CustomerOrderResponse(
                order.getId(),
                order.getPharmacy().getNome(),
                order.getCreatedAt(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getPrescriptionUrl(),
                items
        );
    }

    private CustomerOrderItemResponse mapItemToResponse(OrderItem item) {
        return new CustomerOrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getNome(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}