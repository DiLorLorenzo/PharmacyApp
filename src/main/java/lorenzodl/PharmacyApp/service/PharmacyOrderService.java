package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.PharmacyOrderItemResponse;
import lorenzodl.PharmacyApp.dto.PharmacyOrderResponse;
import lorenzodl.PharmacyApp.entities.OrderItem;
import lorenzodl.PharmacyApp.entities.OrderStatus;
import lorenzodl.PharmacyApp.entities.Pharmacy;
import lorenzodl.PharmacyApp.entities.PurchaseOrder;
import lorenzodl.PharmacyApp.repository.PharmacyRepository;
import lorenzodl.PharmacyApp.repository.PurchaseOrderRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyOrderService {

    private final PharmacyRepository pharmacyRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public List<PharmacyOrderResponse> getMyOrders(String email) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        return purchaseOrderRepository.findByPharmacyId(pharmacy.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PharmacyOrderResponse updateOrderStatus(String email, Long orderId, String status) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        if (!order.getPharmacy().getId().equals(pharmacy.getId())) {
            throw new RuntimeException("Non puoi modificare un ordine di un'altra farmacia");
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Stato ordine non valido");
        }

        order.setStatus(newStatus);
        PurchaseOrder updated = purchaseOrderRepository.save(order);

        return mapToResponse(updated);
    }

    public PurchaseOrder getOrderEntityForPrescription(String email, Long orderId) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        if (!order.getPharmacy().getId().equals(pharmacy.getId())) {
            throw new RuntimeException("Non puoi accedere alla ricetta di un'altra farmacia");
        }

        if (order.getPrescriptionUrl() == null || order.getPrescriptionUrl().isBlank()) {
            throw new RuntimeException("Nessuna ricetta allegata");
        }

        return order;
    }

    public Resource getPrescriptionResource(PurchaseOrder order) {
        try {
            Path filePath = Paths.get(order.getPrescriptionUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File ricetta non trovato");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Errore nel caricamento della ricetta");
        }
    }

    private PharmacyOrderResponse mapToResponse(PurchaseOrder order) {
        String customerName = order.getCustomer().getNome() + " " + order.getCustomer().getCognome();

        List<PharmacyOrderItemResponse> items = order.getItems().stream()
                .map(this::mapItemToResponse)
                .toList();

        return new PharmacyOrderResponse(
                order.getId(),
                customerName,
                order.getCustomer().getEmail(),
                order.getCreatedAt(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getPrescriptionUrl(),
                items
        );
    }

    private PharmacyOrderItemResponse mapItemToResponse(OrderItem item) {
        return new PharmacyOrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getNome(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}