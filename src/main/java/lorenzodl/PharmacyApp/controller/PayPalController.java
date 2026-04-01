package lorenzodl.PharmacyApp.controller;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.CapturePayPalOrderRequest;
import lorenzodl.PharmacyApp.dto.CreatePayPalOrderRequest;
import lorenzodl.PharmacyApp.service.PayPalService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PayPalController {

    private final PayPalService payPalService;

    @PostMapping("/create-order")
    public Map<String, String> createOrder(@RequestBody CreatePayPalOrderRequest request) {
        String orderId = payPalService.createOrder(request.getTotal());
        return Map.of("orderId", orderId);
    }

    @PostMapping("/capture-order")
    public Map<String, String> captureOrder(@RequestBody CapturePayPalOrderRequest request) {
        String status = payPalService.captureOrder(request.getOrderId());
        return Map.of("status", status);
    }
}