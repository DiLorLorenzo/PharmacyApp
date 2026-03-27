package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.UpdatePasswordRequest;
import lorenzodl.PharmacyApp.dto.UpdatePhoneRequest;
import lorenzodl.PharmacyApp.dto.UserProfileResponse;
import lorenzodl.PharmacyApp.entities.User;
import lorenzodl.PharmacyApp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public void updatePassword(String email, UpdatePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("La password attuale non è corretta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void updatePhoneNumber(String email, UpdatePhoneRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
    }
}