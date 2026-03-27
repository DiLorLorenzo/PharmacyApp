package lorenzodl.PharmacyApp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.UpdatePasswordRequest;
import lorenzodl.PharmacyApp.dto.UpdatePhoneRequest;
import lorenzodl.PharmacyApp.dto.UserProfileResponse;
import lorenzodl.PharmacyApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserProfileByEmail(email);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> updatePassword(
            Authentication authentication,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        String email = authentication.getName();
        userService.updatePassword(email, request);
        return ResponseEntity.ok("Password aggiornata con successo");
    }

    @PatchMapping("/me/phone")
    public ResponseEntity<String> updatePhone(
            Authentication authentication,
            @Valid @RequestBody UpdatePhoneRequest request
    ) {
        String email = authentication.getName();
        userService.updatePhoneNumber(email, request);
        return ResponseEntity.ok("Numero di cellulare aggiornato con successo");
    }
}