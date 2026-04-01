package lorenzodl.PharmacyApp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.CreateProductRequest;
import lorenzodl.PharmacyApp.dto.ProductResponse;
import lorenzodl.PharmacyApp.dto.UpdateProductRequest;
import lorenzodl.PharmacyApp.service.PharmacyProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PharmacyProductController {

    private final PharmacyProductService pharmacyProductService;

    @GetMapping
    public List<ProductResponse> getMyProducts(Authentication authentication) {
        String email = authentication.getName();
        return pharmacyProductService.getMyProducts(email);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            Authentication authentication,
            @Valid @RequestBody CreateProductRequest request
    ) {
        String email = authentication.getName();
        ProductResponse response = pharmacyProductService.createProduct(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        String email = authentication.getName();
        ProductResponse response = pharmacyProductService.updateProduct(email, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            Authentication authentication,
            @PathVariable Long id
    ) {
        String email = authentication.getName();
        pharmacyProductService.deleteProduct(email, id);
        return ResponseEntity.ok("Prodotto eliminato con successo");
    }
}