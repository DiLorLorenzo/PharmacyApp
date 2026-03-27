package lorenzodl.PharmacyApp.controller;
import lorenzodl.PharmacyApp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.AuthResponse;
import lorenzodl.PharmacyApp.dto.LoginRequest;
import lorenzodl.PharmacyApp.dto.RegisterCustomerRequest;
import lorenzodl.PharmacyApp.dto.RegisterPharmacyRequest;
import lorenzodl.PharmacyApp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(
            @Valid @RequestBody RegisterCustomerRequest request
    ) {
        AuthResponse response = authService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/pharmacy")
    public ResponseEntity<AuthResponse> registerPharmacy(
            @Valid @RequestBody RegisterPharmacyRequest request
    ) {
        AuthResponse response = authService.registerPharmacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}