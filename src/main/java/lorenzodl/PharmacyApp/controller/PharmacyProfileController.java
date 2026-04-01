package lorenzodl.PharmacyApp.controller;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.PharmacyProfileResponse;
import lorenzodl.PharmacyApp.service.PharmacyProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pharmacy/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PharmacyProfileController {

    private final PharmacyProfileService pharmacyProfileService;

    @GetMapping
    public PharmacyProfileResponse getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return pharmacyProfileService.getMyProfile(email);
    }
}