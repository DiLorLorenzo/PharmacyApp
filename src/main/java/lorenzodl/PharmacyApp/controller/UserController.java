package lorenzodl.PharmacyApp.controller;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.UserProfileResponse;
import lorenzodl.PharmacyApp.service.UserService;
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
}