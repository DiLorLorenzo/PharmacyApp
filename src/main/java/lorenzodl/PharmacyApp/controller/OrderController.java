package lorenzodl.PharmacyApp.controller;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.entities.PurchaseOrder;
import lorenzodl.PharmacyApp.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PurchaseOrder> createOrder(
            Authentication authentication,
            @RequestPart("order") String orderJson,
            @RequestPart(value = "prescription", required = false) MultipartFile prescription
    ) {
        String email = authentication.getName();
        PurchaseOrder order = orderService.createOrder(email, orderJson, prescription);
        return ResponseEntity.ok(order);
    }
}