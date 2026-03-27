package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.UserProfileResponse;
import lorenzodl.PharmacyApp.entities.User;
import lorenzodl.PharmacyApp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return new UserProfileResponse(
                user.getNome(),
                user.getCognome(),
                user.getCodiceFiscale(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name()
        );
    }
}