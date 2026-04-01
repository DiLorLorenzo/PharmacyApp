package lorenzodl.PharmacyApp.controller;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.CustomerOrderResponse;
import lorenzodl.PharmacyApp.service.CustomerOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @GetMapping
    public List<CustomerOrderResponse> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        return customerOrderService.getMyOrders(email);
    }
}