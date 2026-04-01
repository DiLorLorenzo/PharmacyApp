package lorenzodl.PharmacyApp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.PharmacyOrderResponse;
import lorenzodl.PharmacyApp.dto.UpdateOrderStatusRequest;
import lorenzodl.PharmacyApp.entities.PurchaseOrder;
import lorenzodl.PharmacyApp.service.PharmacyOrderService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PharmacyOrderController {

    private final PharmacyOrderService pharmacyOrderService;

    @GetMapping
    public List<PharmacyOrderResponse> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        return pharmacyOrderService.getMyOrders(email);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PharmacyOrderResponse> updateOrderStatus(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        String email = authentication.getName();
        PharmacyOrderResponse response =
                pharmacyOrderService.updateOrderStatus(email, id, request.getStatus());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/prescription")
    public ResponseEntity<Resource> getPrescription(
            Authentication authentication,
            @PathVariable Long id
    ) {
        String email = authentication.getName();

        PurchaseOrder order = pharmacyOrderService.getOrderEntityForPrescription(email, id);
        Resource resource = pharmacyOrderService.getPrescriptionResource(order);

        String fileName = order.getPrescriptionFileName() != null
                ? order.getPrescriptionFileName()
                : "prescription";

        String contentType = order.getPrescriptionContentType() != null
                ? order.getPrescriptionContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}